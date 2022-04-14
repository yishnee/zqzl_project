# zqzl_project

开发团队：ZQZL

团队人员：周智锐 邱杭祺 周津葳 林雯雯

### 独立工具:
port:
- 8084

index上传连接: 
- http://localhost:8084/PdfToEpub/upload/one
- http://localhost:8084/PdfToEpub/upload/many
    
index下载连接:
- http://localhost:8084/PdfToEpub/download/one

sql: 
- jdbc:mysql://localhost:3306/conveter?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC

---
### 单体服务器版:
port:
- 8084

index上传连接:
- http://47.97.7.63:8084/PdfToEpub/upload/one
- http://47.97.7.63:8084/PdfToEpub/upload/many

index下载连接:
- http://47.97.7.63:8084/PdfToEpub/download/one

sql: 
- jdbc:mysql://47.97.7.63:3306/conveter?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC

---
### 分布式:
port:
- 8085

sql:
- jdbc:mysql://47.97.7.63:3306/conveter?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC

cloud:
  nacos:
    discovery:
      server-addr: localhost:8848

pom.xml:
- nacos的依赖

index上传连接:
- http://47.97.7.63:10010/PdfToEpub/upload/one
- http://47.97.7.63:10010/PdfToEpub/upload/many

index下载连接:
- http://47.97.7.63:8085/PdfToEpub/upload/one

---
### 文件缓存地址:
#### Windows:
  
    EpubPath: D:\data\pdf\Test\EpubTotal
    PdfPath: D:\data\pdf\Test\PdfTotal
    HtmlPath: D:\data\pdf\Test\HtmlTotal 
    TxtPath: D:\data\pdf\Test\TxtTotal

日志文件位置: 
``` <property name="FILE_PATH" value="/tmp/pdf2epub/logs/spring-log.%d{yyyy-MM-dd}.%i.log" />```

---
#### Linux:
  
    EpubPath: /tmp/pdf2epub/EpubTotal
    PdfPath: /tmp/pdf2epub/PdfTotal
    HtmlPath: /tmp/pdf2epub/HtmlTotal
    TxtPath: /tmp/pdf2epub/TxtTotal

日志文件位置: 
```<property name="FILE_PATH" value="D:/data/logs/spring-log.%d{yyyy-MM-dd}.%i.log" />```
