# NaoMoreUnit For Intellij

Fork of [MoreUnit For Intellij plugin](https://github.com/MoreUnit/org.moreunit.intellij.plugin)

Installation de l'environnement de développement
---


1. Installer le JDK 1.8 (OpenJDK 1.8 est normalement déjà présent si vous utilisiez Eclipse - :warning: le jdk, pas la jre)
2. Installer la [version community de Intellij](https://www.jetbrains.com/idea/download/#section=linux)
3. Installer Gradle (** :warning: version 4.9**). [Cf. doc](https://docs.gradle.org/current/userguide/installation.html#_step_1_link_xl_href_https_gradle_org_releases_download_link_the_latest_gradle_distribution)
4. Cloner ce repository
5. Lancer Intellij
   1. Choisir d'importer un projet
   2. Sélectionner le dossier où le repository a été cloné
   3. Choisir **Import project from external model** => **Gradle**
   4. Choisir **Use local gradle distribution**
   5. Pour **Gradle home** saisir **/opt/gradle/gradle-4.9**
   6. Pour **Gradle JVM** choisir **JDK 1.8**
6. Vérifier que tout est ok en sélectionnant **Gradle** dans le menu **view > Tool windows** puis en lancant la task **Tasks > intellij > runIde**. Une instance d'IntelliJ sera lancée avec le plugin activé.     

Builder le plugin
---

1. Bumper la version dans le fichier **build.gradle**
2. Afficher la **tool window gradle** (cf. point 6 de l'install de l'env dez dev)
3. Lancer depuis la task **Tasks > intellij > buildPlugin** (accessible sur la barre de droite)
