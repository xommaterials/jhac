# Java client for SAP Hybris administration console

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

## Impex

Import or export data via impex.

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
        <groupId>com.github.klaushauschild1984</groupId>
        <artifactId>jhac</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>
</dependencies>
```
