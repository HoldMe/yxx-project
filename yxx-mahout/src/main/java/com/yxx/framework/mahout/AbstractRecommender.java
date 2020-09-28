package com.yxx.framework.mahout;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;

/**
 * <p>
 * desc 推荐抽象
 * </p>
 *
 * @author wangpan
 * @date 2020/9/27
 */
public abstract class AbstractRecommender {

    public abstract List<RecommendedItem> recommender(String path) throws Exception;
}
