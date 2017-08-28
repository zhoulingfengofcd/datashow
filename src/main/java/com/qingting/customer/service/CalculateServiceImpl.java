package com.qingting.customer.service;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.stereotype.Service;

import com.qingting.customer.baseserver.EquipService;
import com.qingting.customer.baseserver.FilterGroupService;
import com.qingting.customer.baseserver.FilterService;
import com.qingting.customer.baseserver.FormulaService;
import com.qingting.customer.baseserver.MicroFormulaService;
import com.qingting.customer.baseserver.MonitorService;
import com.qingting.customer.baseserver.WaterAreaService;
import com.qingting.customer.baseserver.WaterQualityService;
import com.qingting.customer.baseserver.common.CacheUtil;
import com.qingting.customer.common.pojo.cache.EquipParam;
import com.qingting.customer.common.pojo.common.FilterOrder;
import com.qingting.customer.common.pojo.common.FormulaVariate;
import com.qingting.customer.common.pojo.hbasedo.Equip;
import com.qingting.customer.common.pojo.hbasedo.Filter;
import com.qingting.customer.common.pojo.hbasedo.FilterGroup;
import com.qingting.customer.common.pojo.hbasedo.Formula;
import com.qingting.customer.common.pojo.hbasedo.MicroFormula;
import com.qingting.customer.common.pojo.hbasedo.Monitor;
import com.qingting.customer.common.pojo.hbasedo.WaterArea;
import com.qingting.customer.common.pojo.hbasedo.WaterQuality;
import com.smart.mvc.cache.RedisCache;
@Service("calculateService")
public class CalculateServiceImpl implements CalculateService {
	/*@Resource
	private RedisCache<Equip> equipRedisCache;
	@Resource
	private RedisCache<WaterArea> waterAreaRedisCache;
	@Resource
	private RedisCache<WaterQuality> waterQualityRedisCache;
	@Resource
	private RedisCache<FilterGroup> filterGroupRedisCache;
	@Resource
	private RedisCache<Formula> formulaRedisCache;
	@Resource
	private RedisCache<MicroFormula> microFormulaRedisCache;*/
	
	private static RedisCache<Monitor> monitorRedisCache;
	
	//@Resource
	//private RedisCache<RedisEquipParam> paramRedisCache;
	
	/*@Resource 
	private MonitorService monitorService;
	@Resource 
	private EquipService equipService;
	@Resource
	private WaterAreaService waterAreaService;
	@Resource
	private WaterQualityService waterQualityService;
	
	@Resource
	private FilterGroupService filterGroupService;
	@Resource
	private FilterService filterService;
	@Resource
	private FormulaService formulaService;
	
	@Resource
	private MicroFormulaService microFormulaService;*/
	
	private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript"); 

	
	public void setMonitorRedisCache(RedisCache<Monitor> monitorRedisCache) {
		CalculateServiceImpl.monitorRedisCache = monitorRedisCache;
	}
	
	
	@Override
	public Monitor getResult(Monitor monitor) {
		EquipParam equipParam=getParam(monitor.getEquipCode());
		if(equipParam!=null){
			/****************寿命和预警计算*****************/
			Monitor hisMonitor = monitorRedisCache.get("monitor/"+monitor.getEquipCode());
			System.out.println("历史hisMonitor:"+hisMonitor);
			Monitor newMonitor = compute(equipParam,hisMonitor,monitor);
			monitorRedisCache.set("monitor/"+monitor.getEquipCode(), newMonitor);
			
			/****************预警判断************/
			System.out.println("newMonitor:"+newMonitor);
			Equip equip = CacheUtil.getEquip(newMonitor.getEquipCode());
			System.out.println("equip:"+equip);
			FilterGroup filterGroup = CacheUtil.getFilterGroup(equip.getFilterGroupId());
			System.out.println("filterGroup:"+filterGroup);
			
			//第一级滤芯寿命到期
			Filter oneFilter = CacheUtil.getFilter(filterGroup.getOneFilterId());
			System.out.println("filter:"+oneFilter);
			if(newMonitor.getOneResult()>oneFilter.getLifeTime()){
				
			}
			//第二级滤芯寿命到期
			Filter twoFilter = CacheUtil.getFilter(filterGroup.getTwoFilterId());
			if(newMonitor.getTwoResult()>twoFilter.getId()){
				
			}
			//第三级滤芯寿命到期
			Filter threeFilter = CacheUtil.getFilter(filterGroup.getThreeFilterId());
			if(newMonitor.getThreeResult()>threeFilter.getLifeTime()){
				
			}		
			//第四级滤芯寿命到期
			Filter fourFilter = CacheUtil.getFilter(filterGroup.getFourFilterId());
			if(newMonitor.getFourResult()>fourFilter.getLifeTime()){
				
			}
			//第五级滤芯寿命到期
			Filter fiveFilter = CacheUtil.getFilter(filterGroup.getFiveFilterId());
			if(newMonitor.getFiveResult()>fiveFilter.getLifeTime()){
				
			}
			//微生物超标
			MicroFormula microFormula = CacheUtil.getMicroFormula(filterGroup.getMicroId());
			if(newMonitor.getMicroResult()>microFormula.getLimit()){
				
			}
			
			return newMonitor;
		}else{
			return monitor;
		}
	}
	private Monitor compute(EquipParam equipParam,Monitor hisMonitor,Monitor monitor){
		
		
		//for (FormulaVariate fv : FormulaVariate.values()) {
			
			/*switch(fv.getValue()){
				case FormulaVariate.LAST.
			}*/
		try {
			String oneFormula=equipParam.getOneFormula();
			oneFormula = variateReplace(oneFormula,equipParam,hisMonitor,monitor);
			monitor.setOneResult( (float)((double)scriptEngine.eval(oneFormula)) );
			
			String twoFormula=equipParam.getTwoFormula();
			twoFormula = variateReplace(twoFormula,equipParam,hisMonitor,monitor);
			monitor.setTwoResult( (float)((double)scriptEngine.eval(twoFormula)) );
			
			String threeFormula=equipParam.getThreeFormula();
			threeFormula = variateReplace(threeFormula,equipParam,hisMonitor,monitor);
			monitor.setThreeResult( (float)((double)scriptEngine.eval(threeFormula)) );
			
			String fourFormula=equipParam.getFourFormula();
			fourFormula = variateReplace(fourFormula,equipParam,hisMonitor,monitor);
			monitor.setFourResult( (float)((double)scriptEngine.eval(fourFormula)) );
			
			String fiveFormula=equipParam.getFiveFormula();
			fiveFormula = variateReplace(fiveFormula,equipParam,hisMonitor,monitor);
			monitor.setFiveResult( (float)((double)scriptEngine.eval(fiveFormula)) );
			
			String microFormula=equipParam.getMicroFormula();
			microFormula = variateReplace(microFormula,equipParam,hisMonitor,monitor);
			monitor.setMicroResult( (float)((double)scriptEngine.eval(microFormula)) );
			
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		//}
		
		/*//String str="sum` + ( (-a*(Q-Q`)/(t-t`)+b) + (-a*T*T+b*T+c) )*(t-t`)";
		String[] strs = oneFormula.split("\\+|\\-|\\*|\\/|\\(|\\)|\\s+");
		for (String string : strs) {
			if(StringUtils.isBlank(string)){
				System.out.println(string+".");
			}
		}*/
		return monitor;
		
	}
	private String variateReplace(String formula,
			EquipParam equipParam,Monitor hisMonitor,Monitor monitor){
		System.out.println("公式替换前:"+formula);
		for (FormulaVariate fv : FormulaVariate.values()) {
			System.out.println("fv.getValue:"+fv.getValue());
			if(fv.getValue().equals(FormulaVariate.CL.getValue())){
				formula=formula.replaceAll(fv.getValue(), equipParam.getChlorine().toString());
			}else if(fv.getValue().equals(FormulaVariate.LAST.getValue())){
				if(hisMonitor==null || hisMonitor.getOneResult()==null)
					formula=formula.replaceAll(fv.getValue(), "0");
				else
					formula=formula.replaceAll(fv.getValue(), hisMonitor.getOneResult().toString());
			}else if(fv.getValue().equals(FormulaVariate.Q.getValue())){
				formula=formula.replaceAll(fv.getValue(), monitor.getFlow().toString());
			}else if(fv.getValue().equals(FormulaVariate.QL.getValue())){
				if(hisMonitor==null || hisMonitor.getFlow()==null)
					formula=formula.replaceAll(fv.getValue(), "0");
				else
					formula=formula.replaceAll(fv.getValue(), hisMonitor.getFlow().toString());
			}else if(fv.getValue().equals(FormulaVariate.TB.getValue())){
				formula=formula.replaceAll(fv.getValue(), equipParam.getTurbidity().toString());
			}else if(fv.getValue().equals(FormulaVariate.TEMP.getValue())){
				formula=formula.replaceAll(fv.getValue(), monitor.getTemp().toString());
			}else if(fv.getValue().equals(FormulaVariate.TIME.getValue())){
				formula=formula.replaceAll(fv.getValue(), Long.valueOf(monitor.getCollectTime().getTimeInMillis()).toString());
			}else if(fv.getValue().equals(FormulaVariate.TIMEL.getValue())){
				if(hisMonitor==null || hisMonitor.getCollectTime()==null)
					formula=formula.replaceAll(fv.getValue(), Long.valueOf(monitor.getCollectTime().getTimeInMillis()).toString());
				else
					formula=formula.replaceAll(fv.getValue(), Long.valueOf(hisMonitor.getCollectTime().getTimeInMillis()).toString());
			}else if(fv.getValue().equals(FormulaVariate.TP.getValue())){
				formula=formula.replaceAll(fv.getValue(), monitor.getPurTds().toString());
			}else if(fv.getValue().equals(FormulaVariate.TR.getValue())){
				formula=formula.replaceAll(fv.getValue(), monitor.getRawTds().toString());
			}
		}
		System.out.println("公式替换后:"+formula);
		return formula;
	}
	private EquipParam getParam(String equipCode){
		/*RedisEquipParam redisEquipParam=paramRedisCache.get("param"+equipCode);
		if(redisEquipParam==null){//不存在则去库查找，然后缓存
			Equip equip = equipService.getEquip(equipCode);
			
			//设置对应设备的水质
			WaterArea waterArea = waterAreaService.getById(equip.getWaterAreaId());
			WaterQuality waterQuality = waterQualityService.getNewByWaterAreaId(waterArea.getId());
			redisEquipParam=new RedisEquipParam();
			redisEquipParam.setChlorine(waterQuality.getChlorine());
			redisEquipParam.setTurbidity(waterQuality.getTurbidity());
			
			//设置对应的滤芯公式
			FilterGroup filterGroup = filterGroupService.getById(equip.getFilterGroupId());
			Formula oneFormula = formulaService.getByFilterIdAndOrder(filterGroup.getOneFilterId(), FilterOrder.ONE.getOrder());
			redisEquipParam.setOneFormula(oneFormula.getFormula());
			Formula twoFormula = formulaService.getByFilterIdAndOrder(filterGroup.getTwoFilterId(), FilterOrder.TWO.getOrder());
			redisEquipParam.setTwoFormula(twoFormula.getFormula());
			Formula threeFormula = formulaService.getByFilterIdAndOrder(filterGroup.getThreeFilterId(), FilterOrder.THREE.getOrder());
			redisEquipParam.setTwoFormula(threeFormula.getFormula());
			Formula fourFormula = formulaService.getByFilterIdAndOrder(filterGroup.getFourFilterId(), FilterOrder.FOUR.getOrder());
			redisEquipParam.setTwoFormula(fourFormula.getFormula());
			Formula fiveFormula = formulaService.getByFilterIdAndOrder(filterGroup.getFiveFilterId(), FilterOrder.FIVE.getOrder());
			redisEquipParam.setTwoFormula(fiveFormula.getFormula());
			
			//Filter oneFilter = filterService.getById(filterGroup.getOneFilterId());
			//Filter twoFilter = filterService.getById(filterGroup.getTwoFilterId());
			//Filter threeFilter = filterService.getById(filterGroup.getThreeFilterId());
			//Filter fourFilter = filterService.getById(filterGroup.getFourFilterId());
			//Filter fiveFilter = filterService.getById(filterGroup.getFiveFilterId());
			
			//微生物公式
			MicroFormula microFormula = microFormulaService.getById(filterGroup.getMicroId());
			redisEquipParam.setMicroFormula(microFormula.getFormula());
			
			System.out.println("redisEquipParam:"+redisEquipParam);
			paramRedisCache.set("param"+equipCode, redisEquipParam);
		}*/
		EquipParam equipParam=new EquipParam();
		
		Equip equip = CacheUtil.getEquip(equipCode);
		
		if(equip.getUserId()!=null && equip.getUserId()>0){
		
			WaterArea waterArea = CacheUtil.getWaterArea(equip.getWaterAreaId());
			WaterQuality waterQuality = CacheUtil.getWaterQuality(waterArea.getId());
			
			equipParam.setChlorine(waterQuality.getChlorine());
			equipParam.setTurbidity(waterQuality.getTurbidity());
			
			FilterGroup filterGroup = CacheUtil.getFilterGroup(equip.getFilterGroupId());
			
			Formula oneFormula = CacheUtil.getFormula(filterGroup.getOneFilterId(), FilterOrder.ONE.getOrder());
			equipParam.setOneFormula(oneFormula.getFormula());
			
			Formula twoFormula = CacheUtil.getFormula(filterGroup.getTwoFilterId(), FilterOrder.TWO.getOrder());
			equipParam.setTwoFormula(twoFormula.getFormula());
			
			Formula threeFormula = CacheUtil.getFormula(filterGroup.getThreeFilterId(), FilterOrder.THREE.getOrder());
			equipParam.setThreeFormula(threeFormula.getFormula());
			
			Formula fourFormula = CacheUtil.getFormula(filterGroup.getFourFilterId(), FilterOrder.FOUR.getOrder());
			equipParam.setFourFormula(fourFormula.getFormula());
			
			Formula fiveFormula = CacheUtil.getFormula(filterGroup.getFiveFilterId(), FilterOrder.FIVE.getOrder());
			equipParam.setFiveFormula(fiveFormula.getFormula());
			
			MicroFormula microFormula = CacheUtil.getMicroFormula(filterGroup.getMicroId());
			equipParam.setMicroFormula(microFormula.getFormula());
			
			return equipParam;
		}else{
			return null;
		}
		/*//查设备
		Equip equip = equipRedisCache.get("equip/"+equipCode);
		if(equip==null){
			equip = equipService.getEquip(equipCode);
			equipRedisCache.set("equip/"+equipCode, equip);
		}
		
		//查水区域
		WaterArea waterArea = waterAreaRedisCache.get("waterArea/"+equip.getWaterAreaId());
		if(waterArea==null){
			waterArea = waterAreaService.getById(equip.getWaterAreaId());
			waterAreaRedisCache.set("waterArea/"+equip.getWaterAreaId(),waterArea);
		}
		
		//查水质
		WaterQuality waterQuality = waterQualityRedisCache.get("waterQuality/"+waterArea.getId());
		if(waterQuality==null){
			waterQuality = waterQualityService.getNewByWaterAreaId(waterArea.getId());
			waterQualityRedisCache.set("waterQuality/"+waterArea.getId(),waterQuality);
		}
		
		equipParam.setChlorine(waterQuality.getChlorine());
		equipParam.setTurbidity(waterQuality.getTurbidity());
		
		//查滤芯组合
		FilterGroup filterGroup = filterGroupRedisCache.get("filterGroup/"+equip.getFilterGroupId());
		if(filterGroup==null){
			filterGroup = filterGroupService.getById(equip.getFilterGroupId());
			filterGroupRedisCache.set("filterGroup/"+equip.getFilterGroupId(),filterGroup);
		}
		//查滤芯计算公式
		Formula oneFormula = formulaRedisCache.get("formula/"+filterGroup.getOneFilterId()+"/"+FilterOrder.ONE.getOrder());
		if(oneFormula==null){
			oneFormula = formulaService.getByFilterIdAndOrder(filterGroup.getOneFilterId(), FilterOrder.ONE.getOrder());
			formulaRedisCache.set("formula/"+filterGroup.getOneFilterId()+"/"+FilterOrder.ONE.getOrder(),oneFormula);
		}
		equipParam.setOneFormula(oneFormula.getFormula());
		
		Formula twoFormula = formulaRedisCache.get("formula/"+filterGroup.getTwoFilterId()+"/"+FilterOrder.TWO.getOrder());
		if(twoFormula==null){
			twoFormula = formulaService.getByFilterIdAndOrder(filterGroup.getTwoFilterId(), FilterOrder.TWO.getOrder());
			formulaRedisCache.set("formula/"+filterGroup.getTwoFilterId()+"/"+FilterOrder.TWO.getOrder(),twoFormula);
		}
		equipParam.setTwoFormula(twoFormula.getFormula());
		
		Formula threeFormula = formulaRedisCache.get("formula/"+filterGroup.getThreeFilterId()+"/"+FilterOrder.THREE.getOrder());
		if(threeFormula==null){
			threeFormula = formulaService.getByFilterIdAndOrder(filterGroup.getThreeFilterId(), FilterOrder.THREE.getOrder());
			formulaRedisCache.set("formula/"+filterGroup.getThreeFilterId()+"/"+FilterOrder.THREE.getOrder(),threeFormula);
		}
		equipParam.setThreeFormula(threeFormula.getFormula());
		
		Formula fourFormula = formulaRedisCache.get("formula/"+filterGroup.getFourFilterId()+"/"+FilterOrder.FOUR.getOrder());
		if(fourFormula==null){
			fourFormula = formulaService.getByFilterIdAndOrder(filterGroup.getFourFilterId(), FilterOrder.FOUR.getOrder());
			formulaRedisCache.set("formula/"+filterGroup.getFourFilterId()+"/"+FilterOrder.FOUR.getOrder(),fourFormula);
		}
		equipParam.setFourFormula(fourFormula.getFormula());
		
		Formula fiveFormula = formulaRedisCache.get("formula/"+filterGroup.getFiveFilterId()+"/"+FilterOrder.FIVE.getOrder());
		if(fiveFormula==null){
			fiveFormula = formulaService.getByFilterIdAndOrder(filterGroup.getFiveFilterId(), FilterOrder.FIVE.getOrder());
			formulaRedisCache.set("formula/"+filterGroup.getFiveFilterId()+"/"+FilterOrder.FIVE.getOrder(),fiveFormula);
		}
		equipParam.setFiveFormula(fiveFormula.getFormula());
		
		//查微生物公式
		MicroFormula microFormula = microFormulaRedisCache.get("microFormula/"+filterGroup.getMicroId());
		if(microFormula==null){
			microFormula = microFormulaService.getById(filterGroup.getMicroId());
			microFormulaRedisCache.set("microFormula/"+filterGroup.getMicroId(),microFormula);
		}
		equipParam.setMicroFormula(microFormula.getFormula());
		
		System.out.println("equipParam:"+equipParam);
		//paramRedisCache.set("param"+equipCode, equipParam);
		
		return equipParam;*/
		
		
	}
}
