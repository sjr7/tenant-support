## 1. 项目概述

用于 Spring Boot 应用的多租户插件，支持以低侵入式的方式对现有应用进行多租户改造。目前已经接入线上服务在测试中。

支持特性如下:

- 低侵入式让现在项目支持多租户。
- 采用注解方式对常见业务进行多租户改造。
- 支持多租户模式，同时代码可以无缝切换回单租户模式下。
-  SpringCache 相关注解的多租户支持。
-  RedisTemplate 、 StringRedisTemplate 大部分函数多租户支持。
-  Redisson 中大部分函数的多租户支持。
-  Quartz 定时任务的多租户支持。
-  @Async 异步业务逻辑的多租户支持。
-  Spring Task 的多租户支持。
- 线程池执行业务场景下的多租户支持。





## 2. 项目背景

由于我司现在项目需要多租户支持，在改造过程中对相关功能单独抽取出来了一个模块，用于多个服务进行共享。插件是根据我司的项目来进行定制的，故不一定适合所有的业务场景。

我们现在面临的问题:

1. 应用是 Spring Boot + Mybatis + PostgreSQL (主要数据库) + MySQL (辅助数据库)， 故进行多租户改造需围绕现在技术栈进行。
2. 由于数据库是 PostgreSQL ，网上的多租户文章基本以 MySQL为主，虽说可以参考思路，但总归是需要做些改动的。
3. 解决多租户 SQL 查询问题只是其中的一部分，我们还有其它的场景也需要解决多租户的，例如比较常见的缓存、定时任务。
4. 由于项目时间也比较久了，积累的业务代码还是稍微比较多，后端代码预计  20w +。如果大面积的改造代码，对系统测试来说是非常困难的。
5. 由于是业务系统，很多功能调用大量报表统计的 SQL 。 不同于常规的互联网应用，管理系统中的报表 SQL 基本都是非常复杂，单表查询的 SQL 基本是看不到的，故如果是对 SQL 加 tenant_id 字段实行起来比较困难。
6. 租户数量不会过多，会根据租户的规模大小进行不同的方案处理，如大租户的话会考虑直接内网单独部署一套，无需考虑太多租户数量过多的问题。
7. 再一个就是任务时间紧且参与人数少。



目前 Github 中已经有不少多租户支持相关的插件了，但是大部分看下来，总结起来如下:

- 采用拦截器对 SQL 进行特殊处理 。 这种做法网上已经有了现成的插件，如 MyBatis Plus 中已经有了多租户插件，开启后即可以对 **简单并且是符合条件** 的SQL 进行多租户条件的拼接。 这种方法我们在以前的电商项目中已经实验过了，由于是对 C 端用户提供的接口，故SQL查询都比较简单一点，这种方案在 C端用户接口下是可行的。
- 使用中间件进行 SQL 路由，如使用 mycat 、shardingsphere以及其它类似的插件进行中间层处理。这种不太合适，我们 SQL 中有跨库关联SQL的情况 ( MySQL 中叫跨 database , PostgreSQL 中叫跨 schema )
- 暂没有看到有提供对其它开源组件的多租户支持方案。 企业项目中定时任务的场景是不可避免的，如 quartz 等。
- 直接对项目进行硬编码的，如原先函数中入参是一个，改造后就将函数的入参改造成两个，每一个函数都侵入式的加一个 tenantId 参数。
- 基于服务网关进行请求转发，网关后面每个租户对应着一整套的服务，根据请求中的信息来转发到不同的租户服务上。
- 挂羊头卖狗肉，写着多租户支持，点进去看代码压根就没有多租户相关的代码。





## 3. 设计思路

### 3.1 多租户实现方式

业界常用的几种多租户方式: 

-  **方案一 :  独立程序独立数据库实例**

  优点:  数据最安全，租户的数据在物理层级隔离。 性能很好。

  缺点:  维护成本很高。

  SQL 调整:  不需要对SQL进行调整。

  

- **方案二 :  共享程序共享数据库**

  优点: 数据相对较安全， 在各方面都比较均衡。 

  缺点: 维护成本较高。

  SQL 调整:  不需要对SQL进行调整。

  

- **方案三 :  共享程序共享数据库表**

  优点:  维护成本最低，不需要同步多套数据库表等信息。

  缺点:  数据故障影响所有租户、数据库表以及实体类中增加租户ID字段。

  SQL 调整: 需要对 SQL 进行调整。



### 3.2 多租户数据库落地方案

方案二



### 3.3 租户切换思路

分为以下几个场景: 



- 场景一 :  前端  > 后端

  登陆前请求携带租户ID参数，登陆成功后后端通过登陆用户信息获取租户。

- 场景二:  后端  > 后端

  - 内网服务调用

    通过 http 进行接口调用，可携带租户ID相关信息去调用接口，内网服务间可默认为信任状态，无需对请求进行鉴权。

  - 公网服务调用

    通过 http 进行接口调用，必须先进行认证授权。完成认证后 每次 http 请求携带认证信息，如 token 等，多租户组件即可正确处理后续业务。

    

  

- 场景二:  后端执行业务逻辑

  从当前线程中获取执行业务的租户或者从参数中获取租户信息。

  



## 4. 插件依赖

开发语言:  JAVA 

运行环境:  JDK8+

插件依赖的框架: 

- Spring Boot 2.4.1   强依赖，因为我们项目都是基于 Spring Boot 开发的。
- MyBatis      DAO 层框架
- Dynamic Datasource   3.3.2   多数据源插件，项目原有引用插件，故继续沿用。
- TTL   阿里团队开源的组件，用于线程池模式下变量传递。
- Spring  JDBC   数据源部分功能会使用到。





## 5. 快速接入

 由于暂时未发布到 Maven 中央仓库，故暂时需要手动打包到本地 Maven 仓库。

1. 下载本项目:  

   ````shell
   gie clone https://github.com/July077/tenant-support
   ````

   

2. 下载后使用 IDEA 或者 Eclipse 中打开，如果打开后 IDE 没有识别到项目，在 IDEA 中可以找到  tenant-support / pom.xml ，然后对着 pom.xml 右键可以找到 **Add as Maven Project** ，点击后 IDEA 将开始索引该项目，稍等片刻可完成索引。

   

3. 在 tenant-support 目录下执行下列命令

   ````shell
   mvn clean install -Dmaven.test.skip=true -Pprod
   ````

   执行成功后可在本地 Maven 仓库中   ``` com > suny > tenant-support ```   中找到对应版本的 Jar 包。

   

4. 在需要接入多租户支持的项目 pom.xml 中添加依赖: 

   ````xml
           <dependency>
               <groupId>com.suny</groupId>
               <artifactId>tenant-support</artifactId>
               <version>对应的版本号</version>
           </dependency>
   ````

   

5. 在 Spring Boot 启动类中添加注解  **@EnabledMultipleTenant**

   

6. 配置好所有的数据源信息。

   

7. 配置默认的租户ID属性，找到 application.yml ，加入以下内容 :

   ````yaml
   tenant:
     defaultTenantId: 默认的租户ID
   ````

   

8. 对相应的业务代码进行微调，如添加注解或者其它相应的改动。具体的用法看下面章节介绍。

   

9. 点击启动项目，查看控制台中是否输出租户相关的日志，同时对相关的业务接口测试。





## 6. 具体用法

### 6.1 切换多租户、单租户模式

- 多租户模式

  在项目启动类上添加 **@EnabledMultipleTenant** 注解。

  

- 单租户模式

​       在项目启动类上添加  **@EnableSingleTenant** 注解

  

### 6.2 Spring Cache 支持

- @Cacheable   支持，无需额外调整。
- @CachePut    支持，无需额外调整。
- @CacheEvict  支持，无需额外调整。 allEntries = true 时只会移除当前租户相关的缓存，不会影响到其他租户缓存。



### 6.3 Spring Task 支持

 在执行业务的函数上额外添加  **@TenantScheduledTask** 注解，注解有以下可选择的参数: 

- onlyMaster    默认值为 **fasle** 。 当将值调整为 **true** 时将只会以主库租户身份去执行业务逻辑。

  

````java
@Component
public class DemoTask {
  
    @TenantScheduledTask
    @Scheduled
    public void execute1() {
       // 执行业务逻辑
    }
    
    
    @TenantScheduledTask(onlyMaster = true)
    @Scheduled
    public void execute2() {
       // 以主库租户的身份执行业务
    }
}

````





### 6.4 Spring Data Redis  支持

主要是支持以下两个常用的工具类中大部分函数，少部分函数不支持，需要在业务代码里面自行调整。

#### 6.4.1 RedisTemplate 支持

#####  6.4.1.1  基础函数支持  
  - [x] hasKey()

  - [x] countExistingKeys()

  - [x] delete()

  - [x] unlink()

  - [x] type()

  - [x] keys()

  - [x] rename()

  - [x] renameIfAbsent()

  - [x] expire()

  - [x] expireAt()

  - [x] persist()

  - [x] move()

  - [x] dump()

  - [x] restore()

  - [x] getExpire()

  - [x] watch()

  - [ ] convertAndSend  暂时不支持，需要手动调整代码适配。

#####   6.4.1.2  valueOps  支持函数
- [x] get()
- [x] append()
- [x] size()
- [x] increment()
- [x] set()
- [x] getAndSet()
- [x] setBit()
- [x] getBit()
- [x] bitField()
- [x] decrement()
- [x] multiSet()
- [x] setIfAbsent()
- [x] multiSetIfAbsent()


#####   6.4.1.3  listOps  支持函数
- [x] index()
- [x] remove()
- [x] size()
- [x] trim()
- [x] set()
- [x] range()
- [x] getOperations()
- [x] rightPopAndLeftPush()
- [x] rightPopAndLeftPush()
- [x] rightPopAndLeftPush()
- [x] rightPushIfPresent()
- [x] leftPushIfPresent()
- [x] rightPush()
- [x] rightPush()
- [x] rightPop()
- [x] rightPop()
- [x] rightPop()
- [x] leftPush()
- [x] leftPush()
- [x] leftPop()
- [x] leftPop()
- [x] leftPop()
- [x] leftPushAll()
- [x] leftPushAll()
- [x] rightPushAll()
- [x] rightPushAll()

#####   6.4.1.4  setOps  支持函数
- [x] remove()
- [x] size()
- [x] pop()
- [x] pop()
- [x] members()
- [x] union()
- [x] union()
- [x] union()
- [x] move()
- [x] scan()
- [x] difference()
- [x] difference()
- [x] difference()
- [x] isMember()
- [x] getOperations()
- [x] distinctRandomMembers()
- [x] intersectAndStore()
- [x] intersectAndStore()
- [x] intersectAndStore()
- [x] differenceAndStore()
- [x] differenceAndStore()
- [x] differenceAndStore()
- [x] unionAndStore()
- [x] unionAndStore()
- [x] unionAndStore()
- [x] randomMember()
- [x] randomMembers()
- [x] intersect()
- [x] intersect()
- [x] intersect()

#####   6.4.1.5  streamOps  应用场景少，暂不适配



#####   6.4.1.6  zSetOps
- [x] add()
- [x] add()
- [x] remove()
- [x] count()
- [x] size()
- [x] removeRange()
- [x] range()
- [x] scan()
- [x] reverseRange()
- [x] getOperations()
- [x] score()
- [x] zCard()
- [x] intersectAndStore()
- [x] intersectAndStore()
- [x] intersectAndStore()
- [x] intersectAndStore()
- [x] rangeByScoreWithScores()
- [x] rangeByScoreWithScores()
- [x] removeRangeByScore()
- [x] reverseRangeByScore()
- [x] reverseRangeByScore()
- [x] reverseRangeWithScores()
- [x] reverseRangeByScoreWithScores()
- [x] reverseRangeByScoreWithScores()
- [x] unionAndStore()
- [x] unionAndStore()
- [x] unionAndStore()
- [x] unionAndStore()
- [x] rangeByLex()
- [x] rangeByLex()
- [x] incrementScore()
- [x] reverseRank()
- [x] rangeByScore()
- [x] rangeByScore()
- [x] rank()
- [x] rangeWithScores()

#####   6.4.1.7  geoOps  支持函数
- [x] add()
- [x] add()
- [x] add()
- [x] add()
- [x] remove()
- [x] hash()
- [x] position()
- [x] geoRadiusByMember()
- [x] geoRadiusByMember()
- [x] geoRadiusByMember()
- [x] distance()
- [x] distance()
- [x] geoAdd()
- [x] geoAdd()
- [x] geoAdd()
- [x] geoAdd()
- [x] geoDist()
- [x] geoDist()
- [x] radius()
- [x] radius()
- [x] radius()
- [x] radius()
- [x] radius()
- [x] geoHash()
- [x] geoPos()
- [x] geoRadius()
- [x] geoRadius()
- [x] geoRemove()

#####   6.4.1.8  hllOps  支持函数
- [x] add()
- [x] size()
- [x] delete()
- [x] union()



#### 6.4.2 StringRedisTemplate 支持

StringRedisTemplate 跟上面的 RedisTemplate 函数支持是一致的，除了序列化方式并无其他很大差异。




### 6.5 Redisson 支持

 Redisson 目前的版本并不支持多租户，故选择对 Redisson 部分代码进行调整。

相关 issue： https://github.com/redisson/redisson/issues/3462

answer :  dynamic change of configuration is not supported





#### 6.5.1 基础接口函数支持
- [x] FairLock()
- [x] BloomFilter()
- [x] SetMultimapCache()
- [ ] RemoteService() 
- [x] HyperLogLog()
- [x] MapCache()
- [x] SetCache()
- [x] Bucket()
- [x] PriorityBlockingDeque()
- [x] Geo()
- [x] List()
- [x] Stream()
- [x] TransferQueue()
- [x] BoundedBlockingQueue()
- [x] Lock()
- [x] Set()
- [x] Deque()
- [x] ExecutorService()
- [x] PriorityDeque()
- [x] PermitExpirableSemaphore()
- [x] ScoredSortedSet()
- [x] SortedSet()
- [x] BlockingDeque()
- [x] RingBuffer()
- [x] Map()
- [x] SetMultimap()
- [x] ListMultimap()
- [ ] MultiLock()   
- [x] AtomicLong()
- [x] CountDownLatch()
- [x] ReadWriteLock()
- [x] DoubleAdder()
- [x] Script()
- [x] ListMultimapCache()
- [x] PriorityBlockingQueue()
- [x] BitSet()
- [x] BinaryStream()
- [ ] DelayedQueue()
- [x] Topic()
- [x] AtomicDouble()
- [x] PriorityQueue()
- [x] Queue()
- [ ] LiveObjectService() 
- [x] LongAdder()
- [x] RateLimiter()
- [x] Semaphore()
- [x] LexSortedSet()
- [x] LocalCachedMap()
- [ ] RedLock()
- [x] PatternTopic()
- [x] BlockingQueue()

#### 6.5.2. 批量接口
- [x] Queue()
- [x] Map()
- [ ] Script()
- [x] Topic()
- [x] Set()
- [x] Deque()
- [x] AtomicDouble()
- [x] List()
- [x] SetMultimap()
- [x] BlockingDeque()
- [x] LexSortedSet()
- [x] ListMultimap()
- [x] BitSet()
- [x] SetCache()
- [x] HyperLogLog()
- [x] MapCache()
- [x] Geo()
- [x] AtomicLong()
- [x] Stream()
- [x] BlockingQueue()
- [x] Bucket()
- [x] ScoredSortedSet()
- [x] SetMultimapCache()
- [x] ListMultimapCache()



### 6.6 Quartz 支持

Quartz 目前版本并没有找到合适的官方多租户支持方案。故选择对 Quartz 部分代码进行定制。

相关 issue :

​    https://github.com/quartz-scheduler/quartz/issues/599

开启多租户组件后，需要对部分业务代码进行小部分调整  ( 低侵入式调整 )，调整步骤如下: 



1.  定时任务初始化处增加 **@TenantQuartzInitWrapper **注解

````java
    @TenantQuartzInitWrapper
    public void initQuartzTask(){
        // 从 db 或者其他数据来源查询任务
        // 调用 quartz 相关工具类创建或者添加任务
    }
````



如果是项目中初始化逻辑是结合 @PostConstruct 进行初始化，由于 @PostConstruct 执行优先级较高，故目前版本将暂时不处理这个优先级问题，业务层稍微调整下代码达到初始化目的： 

````java
@Service
public class QuartzTaskServiceImpl implements QuartzTaskService{

    @Autowired
    private QuartzTaskService quartzTaskService;
    
    @PostConstruct
    private void init(){
        quartzTaskService.initQuartzTask();
    }
    
    @TenantQuartzInitWrapper
    public void initQuartzTask(){
        // 从 db 或者其他数据来源查询任务
        // 调用 quartz 相关工具类创建或者添加任务
    }
    
    

}

````



2.  让数据库中的 Job 相关值都拼接上租户信息 

````java
    // 注入该 bean 对 quartz任务相关 key 进行处理
    @Autowired
    private QuartzKeyProcessor quartzKeyProcessor;

    public void createJob(Scheduler scheduler, JobsInfo jobsInfo, Class<? extends Job> jobClass) {

        // 将处理后的 JobKey 填充到对应的参数中
     String tenantJobKey = quartzKeyProcessor.processJobKey(项目代码中原始JobKey值);
        // 将处理后的 TriggerKey 填充到对应的参数中
     String tenantTriggerKey =  quartzKeyProcessor.processTriggerKey(项目代码中原始TriggerKey);
        // 将处理后的 IdentityKey 填充到对应的参数中
     String tenantIdentityKey = quartzKeyProcessor.processIdentityKey(项目代码中原始IdentityKey);
    
    }
````





3.  任务参数携带租户信息

````java

public void createJob(Scheduler scheduler, JobsInfo jobsInfo, Class<? extends Job> jobClass) {        
      // 初始化业务代码....
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
     
        // 创建或者更新定时任务时增加一个参数 (重要步骤)
        jobDetail.getJobDataMap().put(TenantSupportConstant.REQUEST_HEADER_TENANT_ID, 租户ID);

}


````





### 6.7 本地 Map 等缓存支持

业务系统中本地缓存需要进行多租户处理的地方特征如下 :

1. 系统初始化时缓存了数据库中的数据，如系统字典、参数配置等。
2. 使用了 Map  进行缓存数据。

基于现有业务系统的特点，在对应有使用 Map 缓存的业务代码处进行微调，达到适配多租户的目的。



#### 6.7.1 缓存初始化处理

1. 在初始化函数上增加 **@TenantCycleExecute** 注解。

2. 拆分原有的一个初始化函数为 2 个，一个函数负责初始化，一个函数负责刷新缓存。 (**重要**！！！)

   

```java

@Service
public class CodeCacheServiceImpl implements CodeCacheService {
    @Autowired
    private CodeCacheService codeCacheService;
    
    @PostConstruct
    public void init(){
        codeCacheService.doInit();
    }
    
        
    // 初始化函数
    @TenantCycleExecute
    public void doInit() {
        refresh();
    }

    // 刷新缓存函数
    public void refresh(){
        // 实际的业务缓存逻辑
    }
    
 
    
}

```



#### 6.7.2 Map key 处理

对代码最低调整的方案为在 key 上进行调整，可避免使用 多个 Map 等进行缓存多个租户的数据，例子如下 :

1. 注入 **CacheKeyGenerator**  实例。

2. 调用 map 相关带有 key 的函数时，调用 cacheKeyGenerator.genKey(key) 对 key 进行处理后再进行使用。

   

````java
@Service
public class CodeCacheServiceImpl implements CodeCacheService {

    // 注入 bean 对缓存key进行处理
    @Autowired
    private CacheKeyGenerator cacheKeyGenerator;

    private static final ConcurrentHashMap<String, String> CODE_CACHE = new ConcurrentHashMap<>();

    public void add(String key , String value){
        // 处理key
        String newKey  = cacheKeyGenerator.genKey(key);
        CODE_CACHE.put(newKey , value);
    }
    
    public String get(String key){
         // 处理key
        String newKey  =cacheKeyGenerator.genKey(key);
        CODE_CACHE.get(newKey);
    }
    
    public String delete(String key){
         // 处理key
        String newKey  =cacheKeyGenerator.genKey(key);
        CODE_CACHE.remove(newKey);
    }
    
}
````





### 6.8 切换数据源支持

基础数据源切换功能依赖的是业务系统原有的多数据源组件，对于现有的多租户需求，该多数据源组件未能支持，故选择对该组件的代码进行部分逻辑重写，达到支持多租户的目的。



####  6.8.1 切换数据源

   继续使用 **@Ds** 注解进行切换，这块业务不用做调整。



####  6.8.2 事物

事物分以下几种情况 :

1. 切换的数据源位于同一 Database

   位于同一 Database 中事物不做调整，按照正常的流程添加  **@Transaction** 注解即可。

   

2. 切换的数据源在同一数据库实例下的不同 Database  

   - 可以重写事物管理部分逻辑，将所有的事物放在链表中，提交事物的时候一起提交，如果有环节失败则回滚所有的事物。
   - 使用依赖的多数据源插件注解 **@DSTransactional**

   

3. 切换的数据源在不同机器上的 Database

   涉及到分布式事物，这个属于组件外的功能，需求 XA、TCC 等协议解决问题。





### 6.9 @Async 业务支持

相关业务代码无需调整。

````java
    @Async
    public void asyncExecute() {
      // 业务逻辑
    }
````



### 6.10 自定义线程池执行业务支持

相关业务代码无需调整。





## 7. 插件预留扩展点



### 7.1 租户、数据源数据加载扩展



#### 7.1.1 自定义加载数据源信息

自定义 **DataSourceProviderService** 接口实现类，实现 **loadAll()** 函数即可从外部提供数据源信息注入到组件中。

````java
@Service
public class CustomerDataSourceProviderServiceImpl implements DataSourceProviderService {
   
    @Override
    public List<SysDataSource> loadAll() {
        // 从外部加载数据源信息
    }
}
````



#### 7.1.2 动态调整数据源

注入 **DataSourceNotify**  实例，调用函数对数据源进行调整。 多个应用间应该同步进行调整。



- 添加数据源

  ````java
  @Autowired
  private DataSourceNotify dataSourceNotify;
  
  public void addDataSource(){
     SysDataSource  sysDataSource = new SysDataSource();
     // 数据源信息赋值.....
      
     
     dataSourceNotify.add(sysDataSource)
  }
  ````

  

- 更新数据源

  ````java
  @Autowired
  private DataSourceNotify dataSourceNotify;
  
  public void updateDataSource(){
      SysDataSource  newDataSource = new SysDataSource();
      // 数据源信息赋值.....
      
      dataSourceNotify.update(sysDataSource)
  }
  ````

  

- 删除数据源

  ````java
  @Autowired
  private DataSourceNotify dataSourceNotify;
  
  public void deleteDataSource(){
    String datasourceKey = "数据源KEY";
    dataSourceNotify.remove(datasourceKey)
  }
  ````

  

#### 7.1.3 自定义加载租户信息

自定义 **TenantProviderService** 接口实现类，实现 **loadAll()** 函数可从外部加载租户信息注入到组件中。

````java
@Component
public class CustomerTenantProviderServiceImpl implements TenantProviderService {
   
    @Override
    public List<SysTenant> loadAll() {
       // 自定义租户信息
    }
}
````



#### 7.1.4 动态添加租户

注入  **TenantNotify**  实例，调用相关函数对租户信息进行调整。 多个应用间应该同步进行调整。



- 添加租户

  ````java
  @Autowired
  private TenantNotify tenantNotify;
  
  public void addTenant(){
      SysTenant newTenant = new SysTenant();
      // 属性赋值
      
      tenantNotify.add(newTenant);
  }
  ````

  

- 删除租户

  ````java
  @Autowired
  private TenantNotify tenantNotify;
  
  public void removeTenant(){
      String tenantId = "12345";
      tenantNotify.remove(tenantId);
  }
  ````

  

- 更新租户

  ````java
  @Autowired
  private TenantNotify tenantNotify;
  
  public void updateTenant(){
      SysTenant updateTenant = new SysTenant();
      // 属性赋值
      
      tenantNotify.update(updateTenant);
  }
  ````

  



### 7.2  业务数据接口扩展点

#### 7.2.1 自定义授权信息扩展

自定义 **AbstractLoginInfoContext** 实现类，实现登陆用户信息获取。

````java
@Component
public class CustomerLoginInfoContextImpl extends AbstractLoginInfoContext {

    @Override
    public String getTenantId() {
        // 自定义租户ID获取实现
    }

}
````





#### 7.2.2 自定义缓存 key 生成策略

自定义 **CacheKeyGenerator** 实现类，实现缓存 Key 的生成策略。

````java
@Component
public class CustomerCacheKeyGeneratorImpl implements CacheKeyGenerator {

    @Override
    public String genKey(String originKey) {
        // 自定义缓存 Key 的生成逻辑
    }
}
````





#### 7.2.3 自定义 Quartz job key、Trigger Key、Identity Key 生成策略

自定义 **QuartzKeyProcessor** 实现类，在对应的函数中对 Key 进行自定义处理

````java
@Component
public class CustomerTenantQuartzKeyProcessor implements QuartzKeyProcessor {

    @Override
    public String processJobKey(String originJobKey) {
        // 自定义 JobKey 逻辑
    }

    @Override
    public String processTriggerKey(String triggerKey) {
        // 自定义 TriggerKey 逻辑
    }

    @Override
    public String processIdentityKey(String originIdentity) {
       // 自定义 identity 逻辑
    }
}
````









## 8. 编译以及开发调试





## 9. 插件限制

### 9.1 @PostConstruct 初始化限制

  由于切面执行优先级问题，当前版本暂时不处理这个问题，后续迭代更新可考虑处理。业务代码中做响应的调整，来达到多租户执行的效果。

````java
@Service
public class A  {
    @Autowired
    private A a;
    
    @PostConstruct
    public void init(){
        // 调用当前实例的 doInit()
        a.doInit();
    }
    
    @TenantCycleExecute
    public void doInit() {
       // 实际的业务初始化逻辑
    }
    
}
````



### 9.2 Aop 限制

尽量不要跟多租户组件中的注解同时使用，如需使用当前业务系统中未出现的注解，可以先了解下对应注解的原理以及实现，避免不必要的冲突。



### 9.3 与 Shiro 结合使用限制

 在 Shiro Realm 中注入 bean 的话 Aop 是会失效的，原因在于 Shiro 会提前初始化依赖的 Bean ，导致相关增加的逻辑并没有插入到 Bean 中。 故在Realm 中如果需要注入 bean ，并且 bean 中带有增强逻辑的话， 必须采用延迟加载 Bean 的方式 :

````java
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	@Lazy
	private A a;
}	
````





### 9.4 RMI 支持限制

多租户插件暂时无法无侵入式支持RMI，RMI接口通常分以下两种情况 :

- 入参使用对象

  在对象中增加租户ID字段，赋值的时候客户端将对象进行包装，屏蔽租户ID字段赋值细节，即可达到无侵入式支持效果。

- 入参使用多个参数进行传递

​        暂无比较好的无侵入式解决方案，必须在接口中显示加入租户ID参数。 后续也可以采用包装对象的方式，对客户端屏蔽租户ID赋值细节。

调整前 :

````java
public void rmiMethod(String a){
    // 执行业务逻辑
}

rmi.rmiMethod("abc");

````

多租户逻辑调整后: 

````java

// 调整过后的代码
public void rmiMethod(String a){
    // 获取租户ID,调用方屏蔽租户ID细节
    String tenantId = xxxx;
    this.rmiMethod(tenantId , a);
}


public void rmiMethod(String tenantId , String a){
    //  执行对应的逻辑
}


rmiWrapper.rmiMethod("abc");

````





### 9.5 跨进程消息通信限制 

多租户插件暂时无法无侵入式支持该场景，需在业务代码中手动进行租户逻辑处理。  结合我们业务系统进行跨进程通信时，分为以下几种情况 :

- 二进制

  在 payload 部分增加 4个字节用于存放租户ID，在解码时取出做对应的业务处理。

- JSON

  直接在 JSON 中增加租户ID字段，在反序列化 JSON 后取出租户ID进行业务处理。

- 字符串文本

  业务系统中有部分逻辑是投递字符串来进行通信的，后续将逐步废弃该方案，改成使用 JSON 构建对象进行数据交互。

  

### 9.6 Redis lua 脚本执行限制

暂不支持对 Redis Script 相关功能进行处理。故在调用相关脚本时，需要手动对脚本中参数进行处理，以免后续业务逻辑执行出现混乱。








## 10. 常见的问题

- 如何卸载插件

  插件支持多租户以及单租户模式的快速切换，卸载插件有以下两种方法: 

  1. 直接将启动类上的注解  `@EnabledMultipleTenant` 切换成  `@EnableSingleTenant` 即可恢复到多租户前的状态。此方法接近于无改造。 (建议使用)

  2. 将 pom 文件中的依赖移除，并且将业务代码中的租户相关的类移除，即可切换到多租户前的状态，这种方法需要稍微调整下代码。(不建议使用)

     

- 业务数据不对或者错乱了

  首先排查数据来源是否存在问题，例如是否用指定租户下的用户进行数据操作。最好的检查办法就是先检查数据库中数据是否就存在问题。

  

- xx 功能坏了，不能够使用

  可登陆接入多租户前的业务系统，登陆相同账号并进行相同操作，查看原来的功能是否可用。

  

- could not find a datasource  xxx   tenant id=   xxxx

  找不到对应的数据源，检查数据源是否配置的有问题，特别是需要检查租户下是否有对应的数据源。

  

- Thread tenant id not found

  首先检查后端业务代码是否没有按照使用说明中约定进行代码调整或者功能编码。

  

- Property 'platform-tenant-id' is required

  目前版本中需在配置文件中提前定义 default-tenant-id 属性。

  

  






## 11. 参考资料

重写的代码基本都是开源组件，最好的参考资料来源于对应的官网或者代码仓库 ，可下载相关代码进行本地调试，排查相关问题。



Spring : https://github.com/spring-projects/spring-framework

Spirng Boot:  https://github.com/spring-projects/spring-boot

Spring Data Redis : https://github.com/spring-projects/spring-data-redis

Quartz : https://github.com/quartz-scheduler

Redisson :  https://github.com/redisson/redisson
