# Using variables lets us switch tools easily
JAVAC = javac
JAVA = java
JAVADOC = javadoc
SHELL = bash

# Where we want the things we build to get put
BIN_DIR = ../bin
DOC_DIR = ../doc

# Make does not offer a recursive wildcard function, so here's one:
rwildcard=$(wildcard $1$2) $(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2))

# Try to compile all .java files anywhere under ./edu/jhu/
# Note that this is a quick and dirty approach, and won't scale
# well for larger projects.
SOURCES := $(call rwildcard,edu/jhu/,*.java)
OBJECTS := $(patsubst %.java, $(BIN_DIR)/%.class, $(SOURCES))

# Package that the driver class resides in
PACKAGE = edu.jhu.zpalmer2.spring2009.ai.hw6

#########
# flags for tools
#########

JAVAC_FLAGS = -d $(BIN_DIR) -Xlint:all
JAVADOC_FLAGS = -d $(DOC_DIR) 
JAVA_FLAGS = -cp $(BIN_DIR)


########
# targets and build rules
########

MAIN_CLASS = ReinforcementLearningMain

# by default (i.e. if no target is specified), 
# use the "compile the source" target
default: $(MAIN_CLASS)

# compile source code into binaries (.class files)
$(MAIN_CLASS): $(SOURCES) | $(BIN_DIR)
	@echo Building $@...
	$(JAVAC) $(JAVAC_FLAGS) $^ 
# NOTE: a leading '@' prevents the line from being printed on STDOUT;
#       if you want to see the commands being run, just delete the first
#       character

# compile javadocs
docs: $(SOURCES) | $(DOC_DIR)
	@echo Building JavaDocs...
	$(JAVADOC) $(JAVADOC_FLAGS) $^

# pass driver class object to JRE
run:
	$(JAVA) $(JAVA_FLAGS) $(PACKAGE).$(MAIN_CLASS) $(JAVA_ARGS)


# make sure binary output directory exists
$(BIN_DIR):
	mkdir $(BIN_DIR);

# make sure document output directory exists
$(DOC_DIR):
	mkdir $(DOC_DIR);

# clean up object files
clean:
	@echo Deleting object files...
	@rm -f $(OBJECTS) 

