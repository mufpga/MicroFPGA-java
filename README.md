<a href="https://mufpga.github.io/"><img src="https://raw.githubusercontent.com/mufpga/mufpga.github.io/main/img/logo_title.png" alt="Overview"/>

</a>

![version](https://img.shields.io/badge/version-3.1.0-blue)[![License](https://img.shields.io/badge/License-BSD_3--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)



# Overview

MicroFPGA is an FPGA-based platform for the electronic control of microscopes. It aims at using affordable FPGA to generate or read signals from a variety of devices, including cameras, lasers, servomotors, filter-wheels, etc. It can be controlled via [Micro-Manager](https://micro-manager.org/MicroFPGA), or its [Java](https://github.com/mufpga/MicroFPGA-java), [Python](https://github.com/mufpga/MicroFPGA-py) and [LabView](https://github.com/mufpga/MicroFPGA-labview) communication libraries, and comes with optional complementary [electronics](https://github.com/mufpga/MicroFPGA-electronics).

Documentation and tutorials are available on [https://mufpga.github.io/](https://mufpga.github.io/).



<img src="https://raw.githubusercontent.com/mufpga/mufpga.github.io/main/img/figs/G_overview.png" alt="Overview"/>

## Content

This repository contains the Java package to control MicroFPGA. To use `Microfpga` in you Java project, you can [download](https://github.com/mufpga/MicroFPGA-java/releases) the compiled `.jar` or build it from source using maven:

``` bash
mvn package -Dmaven.test.skip=true
```

This repository also contains [examples](https://github.com/mufpga/MicroFPGA-java/tree/main/src/main/test/de/embl/rieslab/microfpga/examples) on how to use MicroFPGA.


## Cite us
Joran Deschamps, Christian Kieser, Philipp Hoess, Takahiro Deguchi, Jonas Ries, "MicroFPGA: An affordable FPGA platform for microscope control",
HardwareX 2023 (13): e00407, doi:[10.1016/j.ohx.2023.e00407](https://doi.org/10.1016/j.ohx.2023.e00407).

MicroFPGA-java was written by Joran Deschamps, EMBL (2020).
