一 文件传输核心功能模块
 核心模块主要完成通讯建立，算法协商，数据传输，数据容错处理等核心功能，不包括调度部分。
 虽然已经有Mina作为基础通信中间件的实现，但实现原理和控制方式较为复杂，不适合作为
 核心模块来使用，现在使用Netty5.X作为通信中间件，重新设计流程与接口，旨在提高内聚，
 增强扩展能力，使之成为文件传输系统构建的基础组件。
 
 通讯方式采用二进制通讯方式，使用私有协议栈，不使用类似FTP或者HTTP方式报文，以提高效率
 和扩展性，Netty5.X作为通讯中间件，上层通过命令工厂方式与之通讯，命令模式入口是Netty5.X
 与上层逻辑的边界，使得底层与上层的边界十分明显，也是得底层更容易升级和替换，
 上层提供传输控制的绝大部分实现，并使用回调接口与用户交互。
 
 二 报文类型
  报文类型是基础通讯中至关重要的环节，本着报文简洁承载容量大的目的，文件传输核心模块的报文类型分为以下几类
  1.请求类：
    文件传输请求：发送文件请求，文件块发送请求，消息文本请求，中断传输请求（双发都可能用到），认证相关报文
  2.应答类：
  文件传输请求应答：发送文件请求应答（拒绝或者接受），文件块应答（一般不应答，应答会在文件块出错的情况下应答，或者直接发送结束应答），
  结束应答（成功或者失败）
  
  报文集合：发送请求报文，发送请求应答报文，文件块发送报文，文件块容错报文，传输结束报文，中断传输报文，消息文本报文，认证报文
  
  
  
  
  报文部分
    
    文件传输核心模块使用自定义报文通讯，报文结构为
    
    HEARDER1|HEARDER2|VERSION|CHECKSUMLEN|CHECKSUM|TYPE|BODYLEN|BODY
    
    如果CheckSum 是2个字节，Body为3个字节，则报文结构如下
    
    
    0--------1-------2--------3--------4--------5--------6--------7-------8-------9-----10------11------12------13-----14------15------16-----17
    |HEARDER1|HEARDER2|VERSION|    C H E C K S U M L E N   (4)    |C H E C K S U M|TYPE |        B O D Y L E N  (4)     |      B O D Y (3)     |
    --------------------------------------------------------------------------------------------------------------------------------------------
    |0xFF    |0xFF    |0x01   |             2                     |0x01     0x02  |0x01 |                3              |0x02     0x03    0x04 |
    --------------------------------------------------------------------------------------------------------------------------------------------
  
   CheckSum长度不确定，需要根据使用的校验算法确定，程序内部提供CRC32和MD5两种方式，报文校验的计算和验证适用于在报文出口和入口处进行，上层数据部分的
   处理提供了DataProcessor回调，可以根据实际情况对文件数据进行处理，对文件块内容的的处理与报文校验没有关系。固定报文头长度3bytes+4bytes（int）+1byte（Ver）+4bytes（int）
  报文内的有效数据长度默认为64KB

   报文主要分为以下几种：

   基础报文结构：BaseMessage，其他报文都继承自此类，基础报文定义了报文最基本的属性，在报文未到达上层处理逻辑是，全都是以基础报文的形体现。


 认证相关报文：认证报文只有一个类，其内部定义了认证类型，主要完成双方DH或者ECDH过程，提供主流的加解密算法实现，并支持指定RSA或者ECC公私钥
   实现简洁的DH过程，也可以填充自己的算法。报文认证过程如下：
   
                                              参照DH过程  
      
   文件相关报文：文件发送请求报文，文件请求应答报文，文件块报文，文件指定块请求报文，发送过程完成报文
   
   其报文顺序为:
   1、发送方通过回调函数调用用户规则允许传输后想文件接收方发送文件发送请求报文
   2、文件接收方收到请求报文后通过回调函数调用用户规则允许后检查本地是否具备接收条件，如果具备返回文件请求应答报文，并允许接收，否则返回拒绝接收
   3、发送方读取文件封装成文件块报文，发送给文件接收方，如果接收方发现文件块有问题则返回文件指定块请求报文，发送方收到此类报文检查合法性，会将文件读取位置
   置为要求位置开始传输
   4、文件发送完毕，发送方会发送文件传输完成报文，等待接收方检查，检查通过返回完成报文，不同过则认为失败返回错误信息
   5、发送方收到完成报文后如果队列中没有可执行任务则发送请求关闭通道报文，接收方收到关闭通道请求后关闭通道，发送方关闭。如果队列中有任务则重复1
   
   
   状态及消息报文：发送/接收完成报文  消息报文
   状态报文在传输过程中负责同步状态，保证文件传输的有序进行，消息报文是一类特殊报文，对传输过程没有影响，是发送方和接收方文本通讯的报文，比如发送用户通知等。
   
   
  逻辑控制器部分
  
  文件传输模块首先具备文件传输功能，即将文件从发送方完整的发送到接收方，此外还支持如下功能：
  
  1.断点续传 2.覆盖控制 3.文件整体检验和块校验 4.报文加密 5.报文压缩 6.流量统计与控制 7.通道可重复利用 8.用户扩展与控制
  
  1、断点续传：断点续传是文件传输类产品基本的功能，在此模块中在发送方发送文件传输请求后，接收方会检查是否需要断点续传，如果需要会在报文置fileoffset值，告知
  发送方文件发送起始位置。
  
  2、覆盖控制：通过提交任务时设置是否需要远程覆盖来确定文件存在同名时的操作
  
  3、文件整体校验和块校验：此模块提供两种文件校验方式，文件整体校验即在发送时对文件做HASH校验，结束后重新检验对比校验结果。文件块校验是指可以指定在传输过程中对每一块进行校验
  如果每一块校验值一致，文件整体完整性也有一定保证，当热，两种校验方式可重叠使用。
  
  4、报文加密：得益于安全模块，报文传输过程中可以进行加密，算法也可以任意组合，比如RSA1024+AES128+CRC32 即DH使用的非对称算法使用RSA1024，报文加密采用AES128，报文校验使用CRC32
  
 5、报文压缩：报文压缩可以在传输文本内容或者规律数组时提高传输效率，但传输效率跟两者有关系，一的使用的压缩算法效率，二的带宽。带宽低，选高压缩比，带块高，不压缩，带宽一般，视情况而定。
 
 6、流量统计与控制：由于报文需要通过编解码器进行接收和发送，可以较为简单是实现流量统计与控制。
 
 7、通道复用：在传输过程中通道建立的开销不可小视，为了尽可能的提高效率，需要服用已建立的通道，此模块通过维护一个任务队列，队列可以每个通道一个，也可以多个通道共用一个任务队列
 只要队列中有任务，通道就不用关闭。
 
 8、用户扩展与控制：模块中使用了多个回调与控制接口，方便用户自定义传输控制逻辑和扩展自己的功能，比如在IDataProcessor中定义文件内容自定义处理方式，onStart中定义是否接受文件传输请求
 方便与其他系统和模块集成。
 
 
 文件路由问题：
 文件路由是指在客户端与客户端之间无法直接建立网络连接的情况下数据包需要中间节点进行数据包转发的情况。
 
 文件路由的设计原则：
 1、数据包的转发需要遵循一定的准发路由表，相当于网络设备的路由表
 2、转发的数据包需要携带上一跳和下一跳的信息，方便数据包的返回和路由
 3、转发的数据包在中间节点只做包的有效性检查，不做内容的判断
 4、路由的跳数不应过高，最大不应超过3 hop
  
 文件路由的设计方法：
  
   本着方便管理自主路由的原则，文件路由方案应该易于管理，具有统一管理、注册中心，管理各节点的参数和路由信息。
   路由信息的设计：
   设计目标：节点可以通过路由表知道此数据包的来源和目的，每个数据包都可以按照路由表追溯数据包的来源和目的
   
   方法：使用Stack来实现路由信息，在数据包进行组包时将路由表按照倒序的顺序压入Stack，数据包的来源为第一跳，目的为最后一跳，需要中间节点路由次将节点信息压入Stack中。
     避免路由环路的方法：为了避免数据包中的路由包含重复节点，导致数据包在网络中来回传输，有两种方法可以避免此类情况
     1、主动型：在进行路由表组合时，对路由表进行环路检查，环路的一个必要条件是节点存在不止一个的重发，严格来说，数据包不可能两次通过一个节点（数据包与相应报文不属于同一类），所以在压入Stack时检查
     是否有重复节店即可。
     2、被动型：可以存在环路，但数据组包时不检查是否存在环路，在数据包中加入TTL（time to live）标志，每经过一个节点，TTL值-1，直到TTL==0数据包被丢弃并由处理节点返回反馈报文。
   
    两种方案都可以避免数据包被无限制的转发下去，但出于理管理，实现以及资源有效性和利用率的考虑，还是需要对数据在组包时进行检查，防止不必要的处理，也可以在发送时就明确传输路由。

RouterStack    
-----------------------------------------------------------------------------------
|SourceNode|
|----------|
|Node1     |
|----------|
|Node2     |
|----------|
|TargetNode|
--------------------------------------------------------------------------------------
发送：
-------------------------------------------------------------------------------------
SourceNode-->Node1-->Node2-->TargetNode

接收返回（包括目的和中间节点，主要指报告和状态）
-------------------------------------------------------------------------------------
TargetNode-->Node2-->Node1-->SourceNode



  
 