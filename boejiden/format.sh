#!/bin/sh
clang-format -i FormatContent.java
java FormatContent.java
npx prettier . --write
