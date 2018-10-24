FROM alpine/git as git
RUN mkdir /tmp

WORKDIR /tmp
ARG APP_REPO=https://github.com/MaxTx01/AppForDeploy.git
RUN git clone $APP_REPO

FROM clojure as build
COPY --from=git /tmp/AppForDeploy /tmp/AppForDeploy

WORKDIR /tmp/AppForDeploy
RUN lein ring war

FROM openjdk:8-jre-alpine

RUN cd ./app-for-deploy-mater && lein ring server
