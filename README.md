# English spelling training shell application with OpenAI assistant, written on java 17

## Build and install distribution

> gradle installDist

## Add OpenAI token env variable "OPENAI_TOKEN"
e.g. on Linux
> export OPENAI_TOKEN="your OpenAI token here"

## Run distribution

### interactive mode

*on Linux:*
```
 ./bin/spelling-trainer
 spelling-trainer:>spell-check
```

*on Windows:*
```
 \bin\spelling-trainer.bat
 spelling-trainer:>spell-check
```

### non-interactive mode

*on Linux:*
> ./bin/spelling-trainer spell-check

*on Windows:*
> \bin\spelling-trainer.bat spell-check
