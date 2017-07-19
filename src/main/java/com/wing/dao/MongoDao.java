package com.wing.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCursor;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * @author qingshan.wqs
 * @since 2017/3/25
 */
@Component
public class MongoDao {

	public static final String DB = "indoor_test";
	public static final String COLLECTION_POIEDIT = "pOIEditData";
	public static final String COLLECTION_INDOTASK = "indoTaskData";
	public static final String BH_BIKE = "bikeIndoor";


	@Resource
	private MongoDb mongoDb;


	public void deleteCollection(String collectionName) {
		mongoDb.mongoClient.getDatabase(DB).getCollection(collectionName).drop();
	}


	public void deleteBahuangBikeData(String outterId) {
		mongoDb.mongoClient.getDatabase(DB).getCollection(BH_BIKE).deleteMany(BsonDocument.parse(format("{'OUTTERID': '%s'}", outterId)));
	}

	public void insertBahuangBikeData(List<Map> mapList) {
		List<Document> documents=new ArrayList<Document>();
		for(Map map:mapList){
			documents.add(new Document(map));
		}
		mongoDb.mongoClient.getDatabase(DB).getCollection(BH_BIKE).insertMany(documents);
	}

	public long collectionCount(String collectionName) {
		return mongoDb.mongoClient.getDatabase(DB).getCollection(collectionName).count();
	}

	/**
	 * Find poiedit data from poi edit id
	 * 
	 * @param poiEditId
	 * @return
	 */
	public Document findPoiEdit(Integer taskId,Integer poiEditId,Integer version) {
		return mongoDb.findFirst(DB, COLLECTION_POIEDIT, format("{'editId': %d,'taskId': %d,'version': %d}", poiEditId,taskId,version));
	}

	/**
	 *  获取最新版本POI数据
	 * @param taskId
	 * @param poiEditId
	 * @return
	 */
	public Document findPoiEdit(Integer taskId,Integer poiEditId) {
		Integer version=getLastVersion(taskId);
		return mongoDb.findFirst(DB, COLLECTION_POIEDIT, format("{'editId': %d,'taskId': %d,'version': %d}", poiEditId,taskId,version));
	}

	/**
	 *  按版本获取POI数据
	 * @param taskId
	 * @param version
	 * @return
	 */
	public List<Map> findPois(Integer taskId,Integer version) {
		MongoCursor iterator=mongoDb.find(DB, COLLECTION_POIEDIT, format("{'taskId': %d,'version': %d}",taskId,version)).iterator();
		List<Map> resultList=new ArrayList<>();
		while(iterator.hasNext()){
			resultList.add((Map) iterator.next());
		}
		return resultList;
	}

	/**
	 * 获取最新版POI数据
	 * @param taskId
	 * @return
	 */
	public List<Map> findPois(Integer taskId) {
		Integer version=getLastVersion(taskId);
		MongoCursor iterator=mongoDb.find(DB, COLLECTION_POIEDIT, format("{'taskId': %d,'version': %d}",taskId,version)).iterator();
		List<Map> resultList=new ArrayList<>();
		while(iterator.hasNext()){
			resultList.add((Map) iterator.next());
		}
		return resultList;
	}


	public List<Map> findMaterials(Integer taskId) {
		MongoCursor iterator=mongoDb.find(DB, COLLECTION_INDOTASK, format("{'taskId': %d}",taskId)).iterator();
		List<Map> resultList=new ArrayList<>();
		Map taskData=(Map)iterator.next();

		JSONObject data=(JSONObject) JSONObject.toJSON(taskData);
		JSONArray array=((JSONObject)data.getJSONArray("versionDatas").get(0)).getJSONArray("materials");
		for(int i=0;i<array.size();i++){
			resultList.add((Map)array.get(i));
		}
		return resultList;
	}

	/**
	 * 获取八荒回流数据
	 * @param outterId
	 * @return
	 */
	public List<Map> findBahunagPois(String outterId) {
		MongoCursor iterator=mongoDb.find(DB, BH_BIKE, format("{'OUTTERID': '%s'}",outterId)).iterator();
		List<Map> resultList=new ArrayList<>();
		while(iterator.hasNext()){
			resultList.add((Map) iterator.next());
		}
		return resultList;
	}

	/**
	 * Find task data from indoTaskData
	 * 
	 * @param taskId
	 * @return
	 */
	public Document findIndoTask(Integer taskId) {
		return mongoDb.findFirst(DB, COLLECTION_INDOTASK, format("{'taskId': %d}", taskId));
	}


	/**
	 *  获取最新数据版本
	 * @param taskId
	 * @return
	 */
	public Integer getLastVersion(Integer taskId){
		Document document=findIndoTask(taskId);
		List<Document> versionDatas=(List<Document>)document.get("versionDatas");
		return versionDatas.size();
	}
}
