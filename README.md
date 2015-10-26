# AbtestJavaClient
Java Client for testing ab_testing framework

# Setup
1. Use thrift to generate Java source code from the .thrift file => This thrift file is same as used in ab_testing repository
2. Do the above step by running ```thrift -o target/ --gen java ab_test.thrift```
