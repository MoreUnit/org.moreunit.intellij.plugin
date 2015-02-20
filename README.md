# MoreUnit For Intellij

<img alt="MoreUnit Logo" src="http://moreunit.sourceforge.net/img/logo.png" />

<a title="MoreUnit build on Travis CI" href="https://travis-ci.org/MoreUnit/org.moreunit.intellij.plugin"><img alt="MoreUnit build status" src="https://travis-ci.org/MoreUnit/org.moreunit.intellij.plugin.svg?branch=master" /></a>

MoreUnit assists you in writing unit tests!

Searching for MoreUnit for Eclipse? [Here it is!](http://moreunit.sourceforge.net)

Otherwise, please find our [latest version here!](https://github.com/MoreUnit/org.moreunit.intellij.plugin/releases/latest)


## Why This Plugin?

- Because some people moved from Eclipse to IntelliJ (we did) but miss [MoreUnit](http://moreunit.sourceforge.net).
- Because IntelliJ "Go To Test" does not support all programing languages.
- Because sometimes you write your production code with a language, and your tests with another one.
- Because not all tests are named "*Test".


## Features

- Works for all IntelliJ-based IDEs (AppCode, IDEA, PhpStorm, PyCharm, RubyMine, WebStorm, etc.)
- Jump form your production code to you test code, and vice-versa. To access the command:
    - From the main menu: "Navigate" > "Jump to Test" or "Jump to Test Subject".
    - From the contextual menu: "Go To" > "Jump to Test" or "Jump to Test Subject".
    - From the "Find Action..." pop-up window: "Jump to Test / Test Subject".
    - (It is advised to assign it a binding!)
- Should several candidates be found, a list is displayed for selection.
- Supported naming patterns:
    - lower/upper CamelCase or word separated with hyphens, underscores or even spaces
    - "test", "spec" or "should" suffixes (case insensitive, except for CamelCase style)
    - "test" or "spec" prefixes (case insensitive, except for CamelCase style)
- Writing tests for your Java code in Scala? Your test code does not have to be written in the same
  language as you production code!
- _Work in progress, see the [backlog](#backlog)..._


## Goals and Principles

- Works for all IntelliJ-based IDEs
- Generic support for all languages first, additions for specific languages can then follow
- Zero configuration
- KISS in general
- TDD only
- Continuous delivery: every significant bunch of commits will be deployed as soon as pushed (OK,
  we miss the publication part for now, but it's coming, see the FAQ)


## FAQ

_Q: Why is there no default key binding?_

A: It's almost impossible to find a binding that would not conflict with another existing one, or
such a binding wouldn't be practical anyway. So we prefer to let you choose the one that suits you.


_Q: I can't see MoreUnit in the plugins list, how am I supposed to install it?_

A: We did not setup automatic publication to the IntelliJ plugin repository yet, but we're working
on it. Meanwhile, you can download MoreUnit from the [Releases page]
(https://github.com/MoreUnit/org.moreunit.intellij.plugin/releases).


## Backlog

- When several candidate files are found, order them by pertinence.
- When several candidate files are found, remember your last choice and pre-select it.
- When no candidate file is found, propose to create one at the right place.
- When a wizard already exists to create a test file (Groovy, Java, Scala), delegate the previous
  action to it.
- Allow for customizing content of files created via the previous action, using IntelliJ templates.
- When a production file gets renamed/moved, rename/move its associated test file(s) in mirror.
- Find tests even when test files contain some additional variable parts in their name. For instance,
  `MyClassWhenFooTest` may be a valid file name for a test of `MyClass`.
- (Maybe) allow for restricting test search scope if performance problems arise.
- (Maybe) allow for specifying custom naming patterns.


## Development
### Requirements

Java 6 && Maven 3.2

Wait... seriously, Java 6? Yes, it is actually [required for MacOS users]
(https://intellij-support.jetbrains.com/entries/27854363-IDE-doesn-t-start-after-updating-to-Mac-OS-Yosemite-or-Mavericks).

### Getting Started

1. Check that the project builds: `./build.sh` (will download IntelliJ CE as a Maven dependency,
   then test the code and package the plugin)
2. Download sources of IntelliJ Community Edition into ../idea-ce/:
   ```git clone git://git.jetbrains.org/idea/community.git ../idea-ce```
3. Checkout the tag corresponding to your IntelliJ version. Example:
   ```cd ../idea-ce && git checkout tags/idea/139.1117.1```
4. Open the project within IntelliJ.


### Continuous Integration

Every push triggers a build on [Travis CI](https://travis-ci.org/MoreUnit/org.moreunit.intellij.plugin).
Notifications are sent by email to subscribers when builds fail or get fixed.

Travis configuration can be found in [.travis.yml](.travis.yml).


### Continuous Delivery

We're committed to publish every piece of code as soon as it has been written. That said, not every
commit deserves a publication: reformatting code or fixing a typo in an internal documentation
shouldn't trigger a deployment, and sometimes we would like to have only one deployment for some
related commits. Also, it would be nice not to pollute our users with several versions a day!

For this reason, a version gets created and published only when a tag following the pattern vX.Y.Z
is pushed (with X, Y, Z ~= [0-9]+).  
The build script will detect that and will update the plugin version from the tag before building.
Then, Travis will create a release on Github by attaching the resulting JAR to the version tag.  
The missing part here is the publication to the JetBrains plugin repository, but we're working on it.

This process is a bit uncommon as there is a risk that a tagged version won't pass the build.
Usually, release processes involve first building the version, and then tagging the code. We chose
that way nonetheless for its simplicity. To prevent any surprise from arising, it is advised to
first push the code un-tagged and then, once Travis says everything is OK, to apply the tag and
push it.

And of course, any code that gets pushed should first have passed all tests locally ;-)


### References / Links

- [IntelliJ IDEA Plugin Development](https://confluence.jetbrains.com/display/IDEADEV/PluginDevelopment) (env setup, getting started…)
- [Plugin Development FAQ](https://confluence.jetbrains.com/display/IDEADEV/Plugin+Development+FAQ)
- [IntelliJ IDEA Architectural Overview](https://confluence.jetbrains.com/display/IDEADEV/IntelliJ+IDEA+Architectural+Overview)
- [How to manage development life cycle of IntelliJ plugins with Maven](http://labs.bsb.com/2013/11/how-to-manage-development-life-cycle-of-intellij-plugins-with-maven-2/)
