<a href="https://mufpga.github.io/"><img src="https://raw.githubusercontent.com/mufpga/mufpga.github.io/main/img/logo_title.png" alt="Overview"/>

</a>

![version](https://img.shields.io/badge/version-3.1-blue)



# Overview

MicroFPGA is an FPGA-based platform for the electronic control of microscopes. It aims at using affordable FPGA to generate or read signals from a variety of devices, including cameras, lasers, servomotors, filter-wheels, etc. It can be controlled via [Micro-Manager](https://micro-manager.org/MicroFPGA), or its [Java](https://github.com/mufpga/MicroFPGA-java), [Python](https://github.com/mufpga/MicroFPGA-py) and [LabView](https://github.com/mufpga/MicroFPGA-labview) communication libraries, and comes with optional complementary [electronics](https://github.com/mufpga/MicroFPGA-electronics).

Documentation and tutorials are available on [https://mufpga.github.io/](https://mufpga.github.io/).



<img src="https://raw.githubusercontent.com/mufpga/mufpga.github.io/main/img/figs/G_overview.png" alt="Overview"/>

## Content

This repository contains the Java package to control MicroFPGA. To use `Microfpga` in you Java project, you can add it as a Maven dependency:

```bash
??
```

Alternatively, you can [download](https://github.com/mufpga/MicroFPGA-java/releases) the compiled `.jar` or build it from source using maven:

``` bash
mvn package -Dmaven.test.skip=true
```

This repository also contains [examples](https://github.com/mufpga/MicroFPGA-java/tree/main/src/main/test/de/embl/rieslab/microfpga/examples) on how to use MicroFPGA.

<!---

## Cite us

Deschamps J, Kieser C, Hoess P, Deguchi T and Ries J, 

--->

MicroFPGA-java was written by Joran Deschamps, EMBL (2020).

