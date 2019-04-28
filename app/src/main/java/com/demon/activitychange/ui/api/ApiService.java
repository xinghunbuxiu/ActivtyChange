package com.demon.activitychange.ui.api;


import com.lixh.base.BaseResPose;
import com.vipvideo.bean.AllVideoInfo;
import com.vipvideo.bean.GroupVideoInfo;
import com.vipvideo.bean.MovieTypeBean;
import com.vipvideo.bean.UserBean;
import com.vipvideo.bean.VideoInfoBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by helin on 2016/10/9 17:09.
 */

public interface ApiService {

    @GET("/conds/?worktype=adnativemovie")
    Observable<BaseResPose<MovieTypeBean>> getAllMovieType();

    @GET("/adnativemovie/")
    Observable<BaseResPose<AllVideoInfo>> getMovieByWhere(@QueryMap Map<String, String> where);

    @GET("/wiseselected/?worktype=adnativemovie")
    Observable<BaseResPose<GroupVideoInfo>> getMovieGroup(@Query("version") String version);

    @GET("/xqinfo/")
    Observable<BaseResPose<VideoInfoBean>> getMovieByWorkId(@QueryMap Map<String, String> where);

    //reader  目录
    @GET("/reader/book_info/{id}/small")
    Observable<BaseResPose<String>> getChapterInfoPackage(@Path("id") String id);

    //章节
    @GET("/book/{id}/small/{page}")
    Observable<BaseResPose<String>> getChapterInfo(@Path("id") String id, @Path("page") String page);

    @GET("/login/login/veifys.html")
    Observable<BaseResPose<UserBean>> login(@Query("username") String username, @Query("passwd") String passwd, @Query("imei") String imei);

    @GET("/login/login/create.html")
    Observable<BaseResPose<String>> register(@Query("username") String username, @Query("passwd") String passwd);

    @GET("/login/login/repass.html")
    Observable<BaseResPose<String>> forgetPwd(@QueryMap Map<String, String> where);

    @GET("/sms/send?tpl_id=107511")
    Observable<BaseResPose<String>> sendMobileCode(@QueryMap Map<String, String> where);

    @GET("/login/login/yzcode.html")
    Observable<BaseResPose<String>> validateCode(@QueryMap Map<String, String> where);

    @GET("/sf?pd=happy&openapi=1&from_sf=1&resource_id=5217&group=portal&alr=1&word=tvcenter_portal")
    @Headers({
            "Accept: text/html",
    })
    Observable<BaseResPose<String>> getMainInfo();


    @GET("/api/app/video/ver2/video/searchVideoInfo/")
    Observable<BaseResPose<String>> searchVideoInfo(@Query("currentPage") int currentPage, @Query("pageSize") int pageSize, @Query("searchContent") String searchContent, @Query("entry") int entry);

    @GET("/api/app/video/ver2/video/searchVideoInfoDetail_v2_2/")
    Observable<BaseResPose<String>> searchVideoInfoDetail(@QueryMap Map<String, String> videoInfo);

    /**
     * videoInfoId	7469
     * pageSize	50
     * currentPage	1
     *
     * @param videoInfo
     * @return
     */
    @GET("/api/app/video/ver2/video/searchVideoByPage_v2_2/")
    Observable<BaseResPose<String>> searchVideoByPage(@QueryMap Map<String, String> videoInfo);

    @GET("/api/app/member/ver2/user/login/")
    Observable<BaseResPose<String>> MhLogin(@Query("uuid") String uuid, @Query("model") String model, @Query("invitation") String invitation);

    @GET("/api/app/video/ver2/user/clickPlayVideo_2_5/")
    Observable<BaseResPose<String>> clickPlayVideo(@Query("videoId") int videoId, @Query("playType") int playType, @Query("serverTime") long j, @Query("authKey") String str);

}
