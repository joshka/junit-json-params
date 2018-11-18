# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.3.1] - 2018-11-17
### Changed
- Updated to JUnit 5.3.1

## [1.1.0] - 2018-08-26

### Added
- Handle simplified (single quoted) JSON.
Java strings are easier to write when you don't have to escape double
quotes all the time. If the JSON parsing fails due to an invalid single
quote character, try again with all the quotes replaced with double
quotes. E.g. accept "{'key':'value'}"`


## [1.0.0] - 2018-08-26
Initial release

## [0.0.1] - 2018-05-30