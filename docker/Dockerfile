FROM docker.io/library/centos:7

ARG IMG_NAME
ARG IMG_TAG
ARG LANG=ko_KR.UTF-8
ARG TZ=Asia/Seoul

ENV IMG_NAME=$IMG_NAME
ENV IMG_TAG=$IMG_TAG
ENV LANG=$LANG
ENV TZ=$TZ
ENV BACKEND=git
ENV PORT=8080

ENV SPRINGBOOT_VERSION=$IMG_TAG

COPY adoptopenjdk.repo /etc/yum.repos.d/
RUN if [ -n "$LANG" ]; then \
      eval `echo $LANG | \
        sed -E -e 's/([a-z]+_[a-z]+)\.([a-z0-9_-]+)/localedef -cf\2 -i\1 \1.\2/i'`; \
    fi; \
    if [ -n "$TZ" -a -f /usr/share/zoneinfo/$TZ ]; then \
      ln -sf /usr/share/zoneinfo/$TZ /etc/localtime; \
    fi; \
    yum -y install adoptopenjdk-11-hotspot&& \
    mkdir -p /webapp/config && \
    yum clean all && \
    rm -rf /var/cache/yum

COPY $BOOTAPP /webapp
COPY entrypoint.sh /entrypoint.sh

WORKDIR /webapp
USER nobody:nobody

EXPOSE ${PORT}/tcp
VOLUME /webapp/config

ENTRYPOINT [ "/entrypoint.sh" ]
CMD [ "run" ]
