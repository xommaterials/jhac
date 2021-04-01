package com.sap.hybris.hac.impex;

import com.sap.hybris.hac.Base;
import com.sap.hybris.hac.Configuration;
import com.sap.hybris.hac.exception.CommunicationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;

/**
 * Import / export endpoint.
 *
 * @author Klaus Hauschild
 */
public class ImportExport extends Base<Impex, ImpexResult> {

  private static final String PATH = "/impex";
  private static final String IMPORT = "/import";
  private static final String EXPORT = "/export";

  public ImportExport(final Configuration configuration) {
    super(configuration, String.class);
  }

  public ImpexResult importData(final Impex... impexes) throws CommunicationException {
    final List<String> errors = new ArrayList<>();
    Arrays.stream(impexes)
        .forEach(
            impex -> {
              final Object result = execute(impex, PATH + IMPORT, "");
              final String asString = result.toString();
              final Document resultHtml = Jsoup.parse(asString);
              final List<String> communicationErrors =
                  resultHtml.select(".error").stream() //
                      .map(Element::text) //
                      .collect(Collectors.toList());
              if (!communicationErrors.isEmpty()) {
                throw new CommunicationException(
                    String.join("\n", communicationErrors), impex, null);
              }
              final List<String> error = getError(resultHtml);
              if (!StringUtils.isEmpty(error)) {
                errors.addAll(error);
              }
            });
    return new ImpexResult(errors, emptyList());
  }

  private List<String> getError(final Document resultHtml) {
    final List<String> rawErrors =
        Arrays.stream(resultHtml.select(".impexResult pre").text().split("\n")) //
            .map(String::trim) //
            .filter(StringUtils::hasLength) //
            .collect(Collectors.toList());
    IntStream.range(0, rawErrors.size())
        .forEach(
            i -> {
              final String error = rawErrors.get(i);
              if (Impex.startsWithKeyword(error)) {
                return;
              }
              if (!error.startsWith(",")) {
                // merge with previous error
                final int previous = i - 1;
                if (previous < 0) {
                  return;
                }
                rawErrors.set(previous, rawErrors.get(previous) + "\n" + error);
                rawErrors.set(i, null);
              }
            });
    return rawErrors.stream() //
        .filter(StringUtils::hasLength) //
        .collect(Collectors.toList());
  }

  public ImpexResult exportData(final Impex impex) throws CommunicationException {
    final Object result = execute(impex, PATH + EXPORT, "");
    final String asString = result.toString();
    final Document resultHtml = Jsoup.parse(asString);

    final List<String> error = getError(resultHtml);
    final ImpexResult errorResult = new ImpexResult(error, null);
    if (errorResult.hasError()) {
      return errorResult;
    }

    final List<String> exportResourceNames =
        resultHtml.select("#downloadExportResultData a").stream()
            .map(element -> element.attr("href"))
            .collect(Collectors.toList());

    final RestTemplate restTemplate = prepareRestTemplate(new HttpHeaders(), PATH + EXPORT);
    final List<byte[]> exportResources =
        exportResourceNames.stream()
            .map(
                exportResourceName ->
                    restTemplate
                        .exchange(
                            configuration().getEndpoint() + PATH + "/" + exportResourceName,
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            byte[].class)
                        .getBody())
            .collect(Collectors.toList());

    return new ImpexResult(emptyList(), exportResources);
  }
}
