FROM ubuntu:15.04

ARG APP_REPO=https://github.com/MaxTx01/AppForDeploy.git

RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein --output lein
RUN lein self-install

RUN sudo apt-get intall git
RUN git clone $APP_REPO

RUN cd ./app-for-deploy-mater && lein ring server