# API Template

Spring Boot WebFlux RESTful API


### Create Archetype from Project

> NOTE: Make sure that you remove the existing `dds-api-template/archetype` directory before creating a new archetype.

Create archetype
```bash
	mvn archetype:create-from-project
```

Copy content of `target/generated-sources/archetype` in to `archetype`

Update `archetype/pom.xml`
	- Update version

Update `archetype/src/main/resources/META-INF/maven/archetype-metadata.xml`

Copy `.gitignore` file to `archetype/src/main/resouces/archetype-resources` 


### Install Archetype

A project can be created using this template as a base. There is an `archetype` within the project to do this.

Clone the project and checkout the latest tag.

Install the archetype locally.
```bash
    cd <base_project_dir>/archetype

    mvn clean install
```


### Create Project Using the Archetype

Once the archetype is installed it can be used to create new projects. Use following steps to do so.

- Go to the directory that you need to create the new project in
- Run following command
```bash
    mvn archetype:generate
```
- This will give an interactive mode
- The interactive mode will display a list of installed archetypes to work with
- Look for `lk.dialog.digitization:dds-api-template-archetype` and enter its number to use it


### Known Issues

#### .gitignore not copied

The `.gitignore` file is not copied from archetype to the project.

Add a `.gitignore` file manually before the first commit that has the following content.
```gitignore
HELP.md
target/
TASKS.md
.mvn
**/application.properties
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**
!**/src/test/**

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/

### VS Code ###
.vscode/

```