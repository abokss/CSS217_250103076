#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p build
find src tests -name '*.java' | sort > build/sources.txt
if javac --help 2>&1 | grep -q -- '--release'; then
    javac --release 8 -Xlint:-options -encoding UTF-8 -d build @build/sources.txt
else
    javac -source 8 -target 8 -encoding UTF-8 -d build @build/sources.txt
fi
java -Duser.language=en -Duser.country=US -cp build VerificationTests
for task in 1 2 3 4 5; do
    java -Duser.language=en -Duser.country=US -cp build "task${task}.Task${task}Runner" > "build/task${task}.txt"
    if [ "$task" = 2 ]; then
        sed -E 's/Age: [0-9]+ ms/Age: <elapsed> ms/' "build/task${task}.txt" > build/task2-normalized.txt
        diff -u expected/task2.txt build/task2-normalized.txt
    else
        diff -u "expected/task${task}.txt" "build/task${task}.txt"
    fi
done
printf 'PASS: all five runner output checks\n'
