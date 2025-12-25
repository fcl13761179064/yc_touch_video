# 机器人地址
# 智家QA发布

RobotURL="https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=e6f1a8f4-f775-4a49-bcfd-b23196408fae"

# 项目标题
ProjectTitle="艾拉智家 Android $3 版本更新"
DownloadURL="https://www.pgyer.com/$2"
changelog=$(cat CHANGELOG.MD | head -n 6)


echo $changelog
curl "$RobotURL" \
 -H "Content-Type: application/json" \
 -d "{ \
    \"msgtype\":\"markdown\",\
    \"markdown\": \
        {   \
            \"content\":\" **$ProjectTitle** \n\n\n    Version: $1 \n 下载地址:[$DownloadURL]($DownloadURL) \n\n\n    更新内容：\n > $changelog \n \n\n\n <@69fbc54d2fcc42e19709f6b42a133a02> <@00515f058d3742b0a463a56900b4afeb> <@8152f6bacef34f8ba0668000158712d5>\"
        }   \
    }"

exit 0;