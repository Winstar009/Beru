## Allure TestNG

To generate Allure Report you should perform following steps:

```bash
$ mvnw clean test site
```

Report will be generated to `target/site/allure-maven-plugin` folder. To open the report you can use the following command:

```bash
$ mvnw io.qameta.allure:allure-maven:serve
```

There is another way of generating the report. The generated report can be opened here "target/site/allure-maven-plugin/index.html". The command to generate the report is the following:

```bash
$ mvnw io.qameta.allure:allure-maven:report
```

* [Original](https://github.com/allure-examples/allure-testng-example)