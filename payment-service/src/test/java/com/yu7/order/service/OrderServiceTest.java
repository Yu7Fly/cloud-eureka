package com.yu7.order.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

import java.util.*;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@ComponentScan()
public class OrderServiceTest {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void testRedisString() {
        //存入数据设置缓存时间 TimeUnit.SECONDS 单位:秒
        stringRedisTemplate.opsForValue().set("Yu7", "hello java", 10, TimeUnit.SECONDS);
        //获取值,key不存在返回null
        System.out.println(stringRedisTemplate.opsForValue().get("Yu7"));
        //如果不存在则插入，返回true为插入成功,false失败
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent("Yummy", "77");
        System.out.println("根据是否存在key来插入的结果：" + b);
        //批量插入
        Map<String, String> map = new HashMap<>();
        map.put("批量插入key1", "批量插入value1");
        map.put("批量插入key2", "批量插入value2");
        map.put("批量插入key5", "批量插入value5");
        map.put("批量插入key6", "批量插入value6");
        stringRedisTemplate.opsForValue().multiSet(map);
        System.out.println(stringRedisTemplate.opsForValue().get("批量插入key1") + stringRedisTemplate.opsForValue().get("批量插入key2"));
        //批量插入，如果里面的所有key都不存在，则全部插入，返回true，如果其中一个在redis中已存在，全不插入，返回false
        Map<String, String> batchMap = new HashMap<>();
        batchMap.put("批量插入key3", "批量插入value3");
        batchMap.put("批量插入key4", "批量插入value4");
        Boolean multiSetIfAbsent = stringRedisTemplate.opsForValue().multiSetIfAbsent(batchMap);
        System.out.println("根据是否存在key来批量插入的结果：" + multiSetIfAbsent);
        //批量获取，key不存在返回null
        List<String> stringList = stringRedisTemplate.opsForValue().multiGet(Arrays.asList("批量插入key5", "批量插入key3"));
        //不存在的key会null，不影响其他value获取
        System.out.println("通过key批量获取value:" + stringList);
        //获取指定字符串长度 Sets the bit at offset in value stored at key.
        Long valueLength = stringRedisTemplate.opsForValue().size("批量插入key3");
        System.out.println("key所对应的value的长度为：" + valueLength); //得到的是字节大小
        //原有的值基础上新增字符串到末尾 返回长度同样也是字节
        Integer integer = stringRedisTemplate.opsForValue().append("批量插入key3", "+新增了一段");
        System.out.println("在原有value后新增并返回长度：" + integer);
        //获取原来key键对应的值并重新赋新值 返回原来值
        String str = stringRedisTemplate.opsForValue().getAndSet("批量插入key4", "key4再次重新赋值");
        System.out.println("key4重新赋值,以前的值为：" + str);
        //获取指定key的值进行减1，如果value不是integer类型，会抛异常，如果key不存在会创建一个，默认value为0
        Long yummyValue1 = stringRedisTemplate.opsForValue().decrement("Yummy");
        System.out.println("key为Yummy的value-1后的值为：" + yummyValue1);
        //获取指定key的值进行加1，如果value不是integer类型，会抛异常，如果key不存在会创建一个，默认value为0
        Long yummyValue2 = stringRedisTemplate.opsForValue().increment("Yummy");
        System.out.println("key为Yummy的value+1后的值为：" + yummyValue2);
        Boolean delete = stringRedisTemplate.opsForValue().getOperations().delete("批量插入key1");
        System.out.println("删除key:批量插入Key1的结果为：" + delete);
        //删除多个key并返回删除key的个数
        Long deleteL = stringRedisTemplate.opsForValue().getOperations().delete(Arrays.asList("批量插入key5", "批量插入key6"));
        System.out.println("删除的key的个数：" + deleteL);
    }

    @Test
    void testRedisList() {
        //在变量左边添加元素值如果key不存在会新建，添加成功返回添加后的总个数
        stringRedisTemplate.opsForList().leftPush("ListKey01","ListValue01");
        stringRedisTemplate.opsForList().leftPush("ListKey02","ListValue02");
//        stringRedisTemplate.opsForList().leftPush("ListKey03","ListValue03");
        //新push的在上面
        stringRedisTemplate.opsForList().leftPush("ListKey01","ListValue02");
        stringRedisTemplate.opsForList().leftPush("ListKey01","ListValue03");

        //向左边批量添加参数元素，如果key不存在会新建，添加成功返回添加后的总个数
        Long pushAll = stringRedisTemplate.opsForList().leftPushAll("ListKey01", "value1", "value2", "value3");
        //向集合最右边添加元素如果key不存在会新建，添加成功返回添加后的总个数
        Long rightPush = stringRedisTemplate.opsForList().rightPush("ListKey01", "value4");
        System.out.println("一顿push操作之后的List中index为2的值为："+stringRedisTemplate.opsForList().index("ListKey01",2));
        //向右边批量添加参数元素，如果key不存在会新建，添加成功返回添加后的总个数
        Long pushAll2 = stringRedisTemplate.opsForList().rightPushAll("ListKey01", "value5", "value6", "value7");
        System.out.println("一顿rightPush操作之后的List中index为6的值为："+stringRedisTemplate.opsForList().index("ListKey01",6));
        //向已存在的集合中添加元素返回集合总元素个数 左边
        Long afterTotal = stringRedisTemplate.opsForList().leftPushIfPresent("ListKey01", "value8");
        System.out.println("左边添加元素后返回集合总元素个数："+afterTotal);
        //向已存在的集合中添加元素返回集合总元素个数 右边
        Long rightL = stringRedisTemplate.opsForList().rightPushIfPresent("ListKey01", "value9");
        System.out.println("右边添加元素后返回集合总元素个数："+rightL);
        //获取集合长度
        Long size = stringRedisTemplate.opsForList().size("ListKey01");
        System.out.println("集合的长度为："+size);
        //移除集合中的左边第一个元素返回删除的元素，如果元素为空，该集合会自动删除
        String leftPop = stringRedisTemplate.opsForList().leftPop("ListKey01");
        System.out.println("移除集合中的左边第一个元素并返回删除的元素为："+leftPop);
        //移除集合中右边的元素返回删除的元素，如果元素为空，该集合会自动删除
        String rightPop = stringRedisTemplate.opsForList().rightPop("ListKey01");
        System.out.println("移除集合中的右边第一个元素并返回删除的元素为："+rightPop);
        //移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出
        String leftStr = stringRedisTemplate.opsForList().leftPop("ListKey01", 10, TimeUnit.SECONDS);
        System.out.println("等待时间里移除的元素为："+leftStr);
        //移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出
        String rightStr = stringRedisTemplate.opsForList().rightPop("ListKey01", 10, TimeUnit.SECONDS);
        System.out.println("等待时间里移除的元素为："+rightStr);
        //移除第一个集合右边的一个元素，插入第二个集合左边插入这个元素
        String temp = stringRedisTemplate.opsForList().rightPopAndLeftPush("ListKey01", "ListKey02");
        System.out.println("从一个列表里弹出后又被弹入的元素为："+temp);
        //在集合的指定位置插入元素,如果指定位置已有元素，则覆盖，没有则新增，超过集合下标+n则会报错
        stringRedisTemplate.opsForList().set("ListKey02", 1, "value11");
        //从存储在键中的列表中删除等于值的元素的第一个计数事件count> 0：删除等于从左到右移动的值的第一个元素；
        //count< 0：删除等于从右到左移动的值的第一个元素；count = 0：删除等于value的所有元素
        Long remove = stringRedisTemplate.opsForList().remove("ListKey01", 0, "value1");
        System.out.println("此次删除的元素个数为："+remove);
        //截取集合元素长度，保留长度内的数据 修建操作只保留指定列表中index在(0-3)的元素
        //stringRedisTemplate.opsForList().trim("ListKey01", 0, 3);
        //获取集合指定位置的值
        String listValue =(String) stringRedisTemplate.opsForList().index("ListKey01", 3);
        System.out.println("指定位置index为3的值为："+listValue);
        //获取指定区间的值
        List<String> list = stringRedisTemplate.opsForList().range("ListKey01", 0, 5);
        System.out.println("指定区间(0-8)的值为:"+list.toString());
        //删除指定集合,返回true删除成功
        Boolean delete = stringRedisTemplate.opsForList().getOperations().delete("ListKey02");
        System.out.println("集合ListKey02删除成功了吗？"+delete);
    }

    @Test
    public void redisHash(){
        //新增hashmap值
        stringRedisTemplate.opsForHash().put("Yu7-HashMap","Yu7","Yu7value");
        //以map集合的形式添加键值对
        Map<String,String> map = new HashMap<>();
        map.put("hash-key2","hash-value2");
        map.put("hash-key3","hash-value3");
        stringRedisTemplate.opsForHash().putAll("Yu7-HashMap-All",map);
        //如果变量值存在，在变量中可以添加不存在的的键值对，
        //如果变量不存在，则新增一个变量，同时将键值对添加到该变量添加成功返回true否则返回false
        Boolean b = stringRedisTemplate.opsForHash().putIfAbsent("Yu7-HashMap-All", "hash-key1", "value1");
        System.out.println("向Yu7-HashMap-All中添加之前不存在的键值的结果是："+b);
        //获取指定变量中的hashMap值  取的是HashMap中的value不是key
        List<Object> values =stringRedisTemplate.opsForHash().values("Yu7-HashMap-All");
        System.out.println("获取到的指定变量Yu7-HashMap-AllHashMap的值为："+values);
        //获取变量中的键值对
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("Yu7-HashMap-All");
        //{hash-key2=hash-value2, hash-key3=hash-value3, hash-key1=value1}
        System.out.println("获取到的指定变量的键值对："+entries);
        //获取变量中的指定map键是否有值,如果存在该map键则获取值，没有则返回null
        Object value = stringRedisTemplate.opsForHash().get("Yu7-HashMap-All", "hash-key2");
        System.out.println("获取指定mapYu7-HashMap-All键hash-key2对应的值："+value);
        //获取变量中的键
        Set<Object> keys = stringRedisTemplate.opsForHash().keys("Yu7-HashMap-All");
        System.out.println("从Yu7-HashMap-All获取的键为："+keys);
        //获取变量的长度
        Long size = stringRedisTemplate.opsForHash().size("Yu7-HashMap-All");
        System.out.println("获取到的长度为："+size);
        //使变量中的键以long值的大小进行自增长值必须为Integer类型,否则异常: ERR hash value is not an integer
        Long increment = stringRedisTemplate.opsForHash().increment("Yu7-HashMap-All", "hash-key7", 1);
        System.out.println("给Yu7-HashMap-All中的hash-key2值进行增长："+increment);
        //以集合的方式获取变量中的值
        List<Object> valuelist= stringRedisTemplate.opsForHash().multiGet("Yu7-HashMap-All", Arrays.asList("hash-key1", "hash-key2"));
        System.out.println("以集合的形式取出变量hash-key1,hash-key2中的值："+valuelist);
        // 删除变量中的键值对，可以传入多个参数，删除多个键值对返回删除成功数量
        Long delete = stringRedisTemplate.opsForHash().delete("Yu7-HashMap-All", "hash-key1");
        System.out.println("删除变量中的键值对成功的个数为："+delete);
    }
}