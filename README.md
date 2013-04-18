CLmax
=====

CLmax is an OpenCL binding library for Java.
The main goals of this library are SPEED, scalability via load balancing, and a very small footprint.

Speed is achieved in a number of ways :
- Non-critical java code trimmed to zero in critical areas for a near-zero java usage overhead
- Query cache system to minimize JNI calls
- Automatic load balancing for maximum throughput in a multi-device configuration

Platforms
=========
Windows 32 & 64 bits, Linux 32 & 64 bits, and Mac OSX binaries will be provided
