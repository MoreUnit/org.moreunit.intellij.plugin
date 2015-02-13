# MoreUnit For Intellij

<a title="MoreUnit build on Travis CI" href="https://travis-ci.org/MoreUnit/org.moreunit.intellij.plugin"><img alt="MoreUnit build status" src="https://travis-ci.org/MoreUnit/org.moreunit.intellij.plugin.svg?branch=master" /></a>


## Why This Plugin?

- Because some people moved from Eclipse to IntelliJ (we did) but miss [MoreUnit](http://moreunit.sourceforge.net).
- Because IntelliJ "Go To Test" does not support all programing languages.
- Because sometimes you write your production code with a language, and your tests with another one.
- Because not all tests are named "*Test".


## Features

- Jump form your production code to you test code, and vice-versa. Menu: "Navigate" > "Jump".
(Only support CamelCase naming for now.)
- _Work in progress..._


## Goals and Principles

- Works for all IntelliJ-based IDEs (AppCode, IDEA, PhpStorm, PyCharm, RubyMine, WebStorm, etc.)
- Generic support for all languages first, additions for specific languages can then follow
- Zero configuration
- KISS in general
- TDD only
- Continuous delivery (OK, we miss the deployment part for now, but it's coming, see the FAQ)


## FAQ

_Q: Why is there no default key binding?_

A: It's almost impossible to find a binding that would not conflict with another existing one, or
such a binding wouldn't be practical anyway. So we prefer to let you choose the one that suits you.


_Q: I can't see MoreUnit in the plugins list, how am I supposed to install it?_

A: We did not setup automatic publication to the IntelliJ plugin repository yet, because we would
like to support some more use cases before advertising the plugin.

If you really can't wait for it, checkout the code and run: ```./build.sh```. You can then install
the plugin from ```target/org.moreunit.intellij.plugin-*.jar```


## Development
### Requirements

Java 6 && Maven 3.2

Wait... seriously, Java 6? Yes, it is actually [required for MacOS users](https://intellij-support.jetbrains.com/entries/27854363-IDE-doesn-t-start-after-updating-to-Mac-OS-Yosemite-or-Mavericks).

### Getting Started

1. Check that the project builds: `./build.sh` (will download IntelliJ CE as a Maven dependency, then test the code and package the plugin)
2. Download sources of IntelliJ Community Edition into ../idea-ce/: ```git clone git://git.jetbrains.org/idea/community.git ../idea-ce```
3. Checkout the tag corresponding to your IntelliJ version. Example: ```cd ../idea-ce && git checkout tags/idea/139.1117.1```
4. Open the project within IntelliJ.
