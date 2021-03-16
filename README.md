# Java client for SAP Hybris administration console [![Build Status](https://travis-ci.org/klaushauschild1984/jhac.svg?branch=master)](https://travis-ci.org/klaushauschild1984/jhac)

SAP Hybris offers with its administration console a powerful tool to

* execute code
* query data
* import data

This java client provide possibility to access hac via Java and is ideal for automation or integration.

The API is designed in fluent way.

## Scripting

Execute scripts and perform any operation.

```
hac()
    .scripting()
    .execute(
        Script.builder()
            .script(
                "spring.beanDefinitionNames.each {\n"
                    + "    println it\n"
                    + "}\n"
                    + "return \"Groovy Rocks!\"")
            .build());
```

This will execute the default Groovy script at a local running Hybris instance.

If you manage to produce an execution result parsable into Java base types `ScriptingResult` has various `asXXX()` methods to get execution result as requested type.

## Flexible search

Query data via flexible search or directly via SQL.

```
hac()
    .flexibleSearch()
    .query(
        FlexibleSearchQuery.builder()
            .flexibleSearchQuery("SELECT * FROM { Product }")
            .build());
```

This will query all products from a local running Hybris instance.

```
hac()
    .flexibleSearch()
    .query(
        FlexibleSearchQuery.builder()
            .flexibleSearchQuery("SELECT COUNT(*) FROM { Product }")
            .build())
    count();
```

If you query for just a `COUNT` `QueryResult` has `count()` that will return the queried count.

The entire query result will be HTML encoded. If you are not interested in this, use `QueryResult.revertHtmlMasking()` to convert the results to normal encoding.

## Impex

### Import

```
hac()
    .impex()
    .importData(
        Impex.builder()
            .scriptContent(
                "INSERT_UPDATE user; uid[unique = true]\n"
                    + "; admin")
            .buildImport());
```

### Import chunking

If you import data is huge and very likely that a single web request can handle this due to long response times, you can use built-in chunking.

```
InputStream = ...
hac()
    .impex()
    .importData(
        Impex.builder()
            .scriptContent(inputStream)
            .buildImport().chunked());
```

`Impex.chunked()` will break up the actual Impex into an array of Impexes which will be imported one after another. All results will be collected and returned as a single result. You can also chunk by your own or provide a chunk `Strategy`.

### Export

```
hac()
    .impex()
    .exportData(
        Impex.builder()
            .scriptContent("INSERT_UPDATE user; uid[unique = true]")
            .buildExport());
```

Exported data can be accessed via `impexResult.getExportResources()`.

## Maven dependency

![Release](https://jitpack.io/v/klaushauschild1984/jhac.svg)

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
...
<dependencies>
    <dependency>
        <groupId>com.github.klaushauschild1984.jhac</groupId>
        <artifactId>jhac-core</artifactId>
        <version>1.0-alpha.3</version>
    </dependency>
</dependencies>
```

## Command line interface

Build on top of java client there is a command line interface providing a productive tool for daily tasks. Java 11 and higher is required for execution.

```
Usage: jhac-cli [-C=<configurationFile> | [-e=<endpoint> -u=<username>
                -p=<password>]] [-hv] [--commit] [--debug] <file>
      <file>                 file to process
                               .groovy -> scripting
                               .fxs -> flexible search
                               .sql -> SQL
                               .import -> Impex import
                               .export -> Impex export
  -C, -configuration=<configurationFile>
                             configuration file
      --commit               commit changes
      --debug
  -e, -endpoint=<endpoint>
  -h, --help                 display this help message
  -p, -password=<password>
  -u, -username=<username>
  -v, --version              display version info
```

You can either pass configuration as separate parameters or prepare a configuration JSON to specify targeted endpoint and your credentials. Commit mode is defaulted to `false` but can be activated with `--commit`. If you need detailed information activate debugging with `--debug`.

Use the following configuration file format for option `-C`. Configuration `htaccess` credentials is optional, omit them if not necessary.

```
{
    "endpoint": "endpoint",
    "username": "username",
    "password": "password",
    "htaccess": {
        "username": "htusername",
        "password": "htpassword"
    }
}

```

For simplicity the actual called endpoint (scripting, flexible search or impex) is derived from extension of given processing file.

There is no option to write the output to a file, but you can easily pipe it into a file with `>` and `&>`.

### Downloads

You can also find ready to use downloads at [releases](https://github.com/klaushauschild1984/jhac/releases).

## Interactive user interface

_TODO_
