#!/bin/bash -e

tmpdir='_build_jar'
rm -rf $tmpdir || (exit 0)
mkdir $tmpdir
cat > $tmpdir/manifest.mf <<_EOF
Manifest-Version: 1.0
Main-Class: edu.jhu.zpalmer2.spring2009.ai.hw6.ReinforcementLearningMain
_EOF
mkdir $tmpdir/classes
javac -target 1.7 -sourcepath src -d $tmpdir/classes $(find src -name '*.java')
jar cfm ReinforcementLearning.jar $tmpdir/manifest.mf -C $tmpdir/classes .
rm -rf $tmpdir
