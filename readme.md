# Spring Security
[![Build Status](https://travis-ci.org/kb19900709/spring-security.svg?branch=master)](https://travis-ci.org/kb19900709/spring-security)  
If you've already had experiences of Spring Boot and Spring Security and you're also looking for a solution of RESTful security, supports clustered sessions, please take this repository as reference.  

## Core Modules
- [Spring Boot](https://goo.gl/Y4dtTB)
- [Spring Security](https://goo.gl/D9tf7m)
- [Spring Session](https://goo.gl/Apn8T3)

## Requirements
- Maven
- Redis 2.8.x or above
- jdk 1.8.x or above

## Important
**Please check the yaml at [application.yml](https://goo.gl/5aMYRc) first.**  
If necessary, you can modify the value for your claim.
```yaml
### headerKey:
### Customize HTTP header to convey the current sessionId instead of cookies.
### In this case, the value would be a JWT string which combines sessionId.
headerKey: Authorization
### sessionKey:
### The key of sessionId in JWT string.
sessionKey: sessionId
```

## Testing
### List of major RESTful APIs
| METHOD | URL | REMARK |
| :---- | :---- | :---- |
| POST | /app-login | Request body: **{"userName":"" ,"password":""}**.<br>Response body: **{"authMessage": "", "errorType": "", "currentUserName": ""}**. |
| GET | /test?browser=POSTMAN | Please send the request two times and observe the responses. |
| POST | /app-logout |  |

**Note**:  
Except login method, the rest of methods need to carry JWT string in http header, the header name is defined at [application.yml](https://goo.gl/5aMYRc).

### List of users
| NAME | PASSWORD | ROLES | AUTHS |
| :---- | :---- | :---- | :---- |
| boss | i_m_boss | BOSS,ADMIN | R,W,D |
| admin | 123456 | ADMIN | R,W,D |
| kb | 654321 | MEMBER | R,W |
| guest | guest | GUEST | R |  

You can modify the list of users at [UserAuthProfile.java](https://goo.gl/44oKNK).
