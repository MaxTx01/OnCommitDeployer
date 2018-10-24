FROM alpine/git as git

WORKDIR /tmp
ARG APP_REPO=https://github.com/MaxTx01/AppForDeploy.git
RUN git clone $APP_REPO

FROM clojure as build
COPY --from=git /tmp/AppForDeploy /tmp/AppForDeploy

EXPOSE 8000

WORKDIR /tmp/AppForDeploy
RUN lein ring server