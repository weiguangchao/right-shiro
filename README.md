# right-shiro

## ShiroFilterFactoryBean

类似于MyBatis的SqlSessionFactoryBean

### SecurityManager

很重要

- Authenticator

	- OnceModularRealmAuthenticator

	  找到能够适配当前AuthenticationToken的Realm，并根据找到Realm的个数决定调用相关的方法。其中，多Realm可能会涉及到调用策略，所以要在此进行区分

- Realm

	- PasswordRealm

		- PasswordMatcher

		  匹配用户登陆信息与数据库配置信息是否一致

		- PasswordToken

		  AuthenticationToken的拓展，[auth]模式进行使用

	- jwtRealm

		- JwtMatcher

		  对jwt令牌进行验签

		- JwtToken

		  AuthenticationToken的拓展，[jwt]模式进行使用

- SessionStorageEvaluator

  配置jwt无状态session

- SubjectFactory

  配置始终不创建session

### FilterChainDefinitionMap

配置url->认证规则

### FilterChainResolver

适配自定义的jwt规则（/resource/api==POST），具体的匹配过程会调用Filter进行

### Filter

- PasswordFilter

  匹配auth规则：auth[xxx,xxx,xxx]

- JwtFilter

  匹配jwt规则：jwt[xxx,xxx,xxx]

## shiro-spring maven依赖

<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>${shiro.version}</version>
</dependency>

## web配置

### StartupFilter

配置XssSqlHttpServletRequestWrapper

### XssSqlHttpServletRequestWrapper

实现xss、sql注入过滤

*XMind: ZEN - Trial Version*