# Catalyx-Template

Template repo for creating a mod based on [Catalyx](https://github.com/Ender-Development/Catalyx/).

## References

This template uses:
- [RetroFuturaGradle](https://github.com/GTNewHorizons/RetroFuturaGradle)

This template is loosely based on:
- [CleanroomMC - ForgeDevEnv](https://github.com/CleanroomMC/ForgeDevEnv)
- [CleanroomMC - TemplateDevEnvKt](https://github.com/CleanroomMC/TemplateDevEnvKt)
- [GregTechCEu - Buildscripts](https://github.com/GregTechCEu/Buildscripts)
- [GTNewHorizons - ExampleMod1.7.10](https://github.com/GTNewHorizons/ExampleMod1.7.10)

## Dev environment

- default maven repositories
- default mods for assisting with development
- everything written in Kotlin
- easy to configure / update
- gradle options for version management, GroovyScript options, creating a Reference/Tags class (with stuff like MOD_ID/similar)
- built-in mixin, coremod and access transformer support
- credentials are managed locally instead of using environment variables
- comes with a few handy set-up scripts

## Spotless

This template uses [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle#readme) to format code.
To auto-format code, run the `Apply Spotless` gradle task or execute the `spotlessInstallGitPrePushHook` task to install a git pre-push hook that will format code before each push.
The formatting rules aren't finalized yet as I still need to talk to roz on what the best rules are for our projects. We also recommend using [IntelliJ IDEA](https://www.jetbrains.com/idea/) as IDE as it has the best Kotlin support,
along with the [Ktlint](https://plugins.jetbrains.com/plugin/15057-ktlint) plugin to highlight formatting issues in the IDE.
