# Simple Quality Gate (SQG)

The goal of this project is to improve software quality through static analysis of the source code. It is intended to be used a step on a CI pipeline, where if a code rule fail, the pipeline fails. Currently it has five metrics to be used on Java projects:
- Block size
- File size
- Cyclomatic complexity per block
- Cyclomatic complexity per class
- Quantity of parameters of a method

For this project, the concept of 'block' is anything between brackets inside of a class (method, if/for/while statement, static block, etc).

# Configuration

To configure the Simple Quality Gate is very simple. There is a property file called app.properties which contains the following configurations:
- build.break: Set to true to break the build
- src.root: The root where the SQG should start looking for .java to analyze
- src.encoding: The encoding of the Java files
- rule.'key': Configure the threshold of the rule. The existing keys are:
  - rule.complexity_per_block
  - rule.complexity_per_file
  - rule.file_size
  - rule.block_size
  - rule.rule_quantity_of_parameters
- report.export: Define a file path to save the analysis report on file
- report.block_from_getting_worse: For projects that are already in development, it is recommended to first generate a report using the property report.export and then put the generated file in this property. By doing that, the SQG won't break the build for the files that are already over the threshold, it'll break only if they get worse from their current values.
