package com.example.module_ad.bean;

import com.example.module_ad.base.IBaseAdBean;
import com.example.module_ad.base.IBaseXXBean;

/**
 * @author wujinming QQ:1245074510
 * @name AlarmClock
 * @class name：com.example.module_ad.bean
 * @class describe
 * @time 2020/12/29 10:18:36
 * @class describe
 */
public class AdBean {

    /**
     * code : 0
     * message : 成功
     * data : {"start_page":{"spread_screen":{"status":false,"ad_origin":"gdt_toutiao","times":1,"change_times":300,"ad_percent":"0_100"}},"home_page":{"incentive_video":{"status":false,"ad_origin":"gdt_toutiao","times":0,"change_times":3,"ad_percent":"0_100"}},"skin_page":{"insert_screen":{"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}},"more_page":{"native_screen":{"status":false,"ad_origin":"gdt_toutiao","times":0,"change_times":3,"ad_percent":"0_100"},"insert_screen":{"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}},"exit_page":{"native_screen":{"status":false,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}},"setting_page":{"insert_screen":{"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}},"Advertisement":{"kTouTiaoAppKey":"5130956","kTouTiaoKaiPing":"887417578","kTouTiaoBannerKey":"945710764","kTouTiaoChaPingKey":"945710766","kTouTiaoJiLiKey":"945710777","kTouTiaoSeniorKey":"945710762","kGDTMobSDKAppKey":"1111360142","kGDTMobSDKChaPingKey":"4081750031409458","kGDTMobSDKKaiPingKey":"5091351021203390","kGDTMobSDKBannerKey":"8061756071700423","kGDTMobSDKNativeKey":"1001955011708395","kGDTMobSDKJiLiKey":"5051950021109471"}}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * start_page : {"spread_screen":{"status":false,"ad_origin":"gdt_toutiao","times":1,"change_times":300,"ad_percent":"0_100"}}
         * home_page : {"incentive_video":{"status":false,"ad_origin":"gdt_toutiao","times":0,"change_times":3,"ad_percent":"0_100"}}
         * skin_page : {"insert_screen":{"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}}
         * more_page : {"native_screen":{"status":false,"ad_origin":"gdt_toutiao","times":0,"change_times":3,"ad_percent":"0_100"},"insert_screen":{"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}}
         * exit_page : {"native_screen":{"status":false,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}}
         * setting_page : {"insert_screen":{"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}}
         * Advertisement : {"kTouTiaoAppKey":"5130956","kTouTiaoKaiPing":"887417578","kTouTiaoBannerKey":"945710764","kTouTiaoChaPingKey":"945710766","kTouTiaoJiLiKey":"945710777","kTouTiaoSeniorKey":"945710762","kGDTMobSDKAppKey":"1111360142","kGDTMobSDKChaPingKey":"4081750031409458","kGDTMobSDKKaiPingKey":"5091351021203390","kGDTMobSDKBannerKey":"8061756071700423","kGDTMobSDKNativeKey":"1001955011708395","kGDTMobSDKJiLiKey":"5051950021109471"}
         */

        private StartPageBean start_page;
        private HomePageBean home_page;
        private SkinPageBean skin_page;
        private MorePageBean more_page;
        private ExitPageBean exit_page;
        private SettingPageBean setting_page;
        private AdvertisementBean Advertisement;

        public StartPageBean getStart_page() {
            return start_page;
        }

        public void setStart_page(StartPageBean start_page) {
            this.start_page = start_page;
        }

        public HomePageBean getHome_page() {
            return home_page;
        }

        public void setHome_page(HomePageBean home_page) {
            this.home_page = home_page;
        }

        public SkinPageBean getSkin_page() {
            return skin_page;
        }

        public void setSkin_page(SkinPageBean skin_page) {
            this.skin_page = skin_page;
        }

        public MorePageBean getMore_page() {
            return more_page;
        }

        public void setMore_page(MorePageBean more_page) {
            this.more_page = more_page;
        }

        public ExitPageBean getExit_page() {
            return exit_page;
        }

        public void setExit_page(ExitPageBean exit_page) {
            this.exit_page = exit_page;
        }

        public SettingPageBean getSetting_page() {
            return setting_page;
        }

        public void setSetting_page(SettingPageBean setting_page) {
            this.setting_page = setting_page;
        }

        public AdvertisementBean getAdvertisement() {
            return Advertisement;
        }

        public void setAdvertisement(AdvertisementBean Advertisement) {
            this.Advertisement = Advertisement;
        }

        public static class StartPageBean {
            /**
             * spread_screen : {"status":false,"ad_origin":"gdt_toutiao","times":1,"change_times":300,"ad_percent":"0_100"}
             */

            private SpreadScreenBean spread_screen;

            public SpreadScreenBean getSpread_screen() {
                return spread_screen;
            }

            public void setSpread_screen(SpreadScreenBean spread_screen) {
                this.spread_screen = spread_screen;
            }

            public static class SpreadScreenBean {
                /**
                 * status : false
                 * ad_origin : gdt_toutiao
                 * times : 1
                 * change_times : 300
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }
            }
        }

        public static class HomePageBean implements IBaseAdBean{
            /**
             * incentive_video : {"status":false,"ad_origin":"gdt_toutiao","times":0,"change_times":3,"ad_percent":"0_100"}
             */

            private IncentiveVideoBean incentive_video;

            public IncentiveVideoBean getIncentive_video() {
                return incentive_video;
            }

            public void setIncentive_video(IncentiveVideoBean incentive_video) {
                this.incentive_video = incentive_video;
            }

            @Override
            public IBaseXXBean getBaseBanner_screen() {
                return incentive_video;
            }

            @Override
            public IBaseXXBean getBaseNative_screen() {
                return incentive_video;
            }

            @Override
            public IBaseXXBean getBaseInsert_screen() {
                return incentive_video;
            }

            public static class IncentiveVideoBean implements IBaseXXBean{
                /**
                 * status : false
                 * ad_origin : gdt_toutiao
                 * times : 0
                 * change_times : 3
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return times;
                }
            }
        }

        public static class SkinPageBean  implements IBaseAdBean {
            /**
             * insert_screen : {"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}
             */

            private InsertScreenBean insert_screen;

            public InsertScreenBean getInsert_screen() {
                return insert_screen;
            }

            public void setInsert_screen(InsertScreenBean insert_screen) {
                this.insert_screen = insert_screen;
            }

            @Override
            public IBaseXXBean getBaseBanner_screen() {
                return insert_screen;
            }

            @Override
            public IBaseXXBean getBaseNative_screen() {
                return insert_screen;
            }

            @Override
            public IBaseXXBean getBaseInsert_screen() {
                return insert_screen;
            }

            public static class InsertScreenBean implements IBaseXXBean{
                /**
                 * status : false
                 * ad_origin : gdt_toutiao
                 * times : 300
                 * change_times : 3
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return times;
                }
            }
        }

        public static class MorePageBean implements IBaseAdBean {
            /**
             * native_screen : {"status":false,"ad_origin":"gdt_toutiao","times":0,"change_times":3,"ad_percent":"0_100"}
             * insert_screen : {"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}
             */

            private NativeScreenBean native_screen;
            private InsertScreenBean insert_screen;

            public NativeScreenBean getNative_screen() {
                return native_screen;
            }

            public void setNative_screen(NativeScreenBean native_screen) {
                this.native_screen = native_screen;
            }

            public InsertScreenBean getInsert_screen() {
                return insert_screen;
            }

            public void setInsert_screen(InsertScreenBean insert_screen) {
                this.insert_screen = insert_screen;
            }

            @Override
            public IBaseXXBean getBaseBanner_screen() {
                return native_screen;
            }

            @Override
            public IBaseXXBean getBaseNative_screen() {
                return native_screen;
            }

            @Override
            public IBaseXXBean getBaseInsert_screen() {
                return insert_screen;
            }

            public static class NativeScreenBean implements IBaseXXBean {
                /**
                 * status : false
                 * ad_origin : gdt_toutiao
                 * times : 0
                 * change_times : 3
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return times;
                }
            }

            public static class InsertScreenBean implements IBaseXXBean {
                /**
                 * status : false
                 * ad_origin : gdt_toutiao
                 * times : 300
                 * change_times : 3
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return times;
                }
            }
        }

        public static class ExitPageBean implements IBaseAdBean {
            /**
             * native_screen : {"status":false,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeScreenBean native_screen;

            public NativeScreenBean getNative_screen() {
                return native_screen;
            }

            public void setNative_screen(NativeScreenBean native_screen) {
                this.native_screen = native_screen;
            }

            @Override
            public IBaseXXBean getBaseBanner_screen() {
                return native_screen;
            }

            @Override
            public IBaseXXBean getBaseNative_screen() {
                return native_screen;
            }

            @Override
            public IBaseXXBean getBaseInsert_screen() {
                return native_screen;
            }

            public static class NativeScreenBean implements IBaseXXBean{
                /**
                 * status : false
                 * ad_origin : gdt_toutiao
                 * times : 5
                 * change_times : 10
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return times;
                }
            }
        }

        public static class SettingPageBean implements IBaseAdBean{
            /**
             * insert_screen : {"status":false,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}
             */

            private InsertScreenBean insert_screen;

            public InsertScreenBean getInsert_screen() {
                return insert_screen;
            }

            public void setInsert_screen(InsertScreenBean insert_screen) {
                this.insert_screen = insert_screen;
            }

            @Override
            public IBaseXXBean getBaseBanner_screen() {
                return insert_screen;
            }

            @Override
            public IBaseXXBean getBaseNative_screen() {
                return insert_screen;
            }

            @Override
            public IBaseXXBean getBaseInsert_screen() {
                return insert_screen;
            }

            public static class InsertScreenBean implements IBaseXXBean{
                /**
                 * status : false
                 * ad_origin : gdt_toutiao
                 * times : 300
                 * change_times : 3
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return times;
                }
            }
        }

        public static class AdvertisementBean {
            /**
             * kTouTiaoAppKey : 5130956
             * kTouTiaoKaiPing : 887417578
             * kTouTiaoBannerKey : 945710764
             * kTouTiaoChaPingKey : 945710766
             * kTouTiaoJiLiKey : 945710777
             * kTouTiaoSeniorKey : 945710762
             * kGDTMobSDKAppKey : 1111360142
             * kGDTMobSDKChaPingKey : 4081750031409458
             * kGDTMobSDKKaiPingKey : 5091351021203390
             * kGDTMobSDKBannerKey : 8061756071700423
             * kGDTMobSDKNativeKey : 1001955011708395
             * kGDTMobSDKJiLiKey : 5051950021109471
             */

            private String kTouTiaoAppKey;
            private String kTouTiaoKaiPing;
            private String kTouTiaoBannerKey;
            private String kTouTiaoChaPingKey;
            private String kTouTiaoJiLiKey;
            private String kTouTiaoSeniorKey;
            private String kGDTMobSDKAppKey;
            private String kGDTMobSDKChaPingKey;
            private String kGDTMobSDKKaiPingKey;
            private String kGDTMobSDKBannerKey;
            private String kGDTMobSDKNativeKey;
            private String kGDTMobSDKJiLiKey;

            public String getKTouTiaoAppKey() {
                return kTouTiaoAppKey;
            }

            public void setKTouTiaoAppKey(String kTouTiaoAppKey) {
                this.kTouTiaoAppKey = kTouTiaoAppKey;
            }

            public String getKTouTiaoKaiPing() {
                return kTouTiaoKaiPing;
            }

            public void setKTouTiaoKaiPing(String kTouTiaoKaiPing) {
                this.kTouTiaoKaiPing = kTouTiaoKaiPing;
            }

            public String getKTouTiaoBannerKey() {
                return kTouTiaoBannerKey;
            }

            public void setKTouTiaoBannerKey(String kTouTiaoBannerKey) {
                this.kTouTiaoBannerKey = kTouTiaoBannerKey;
            }

            public String getKTouTiaoChaPingKey() {
                return kTouTiaoChaPingKey;
            }

            public void setKTouTiaoChaPingKey(String kTouTiaoChaPingKey) {
                this.kTouTiaoChaPingKey = kTouTiaoChaPingKey;
            }

            public String getKTouTiaoJiLiKey() {
                return kTouTiaoJiLiKey;
            }

            public void setKTouTiaoJiLiKey(String kTouTiaoJiLiKey) {
                this.kTouTiaoJiLiKey = kTouTiaoJiLiKey;
            }

            public String getKTouTiaoSeniorKey() {
                return kTouTiaoSeniorKey;
            }

            public void setKTouTiaoSeniorKey(String kTouTiaoSeniorKey) {
                this.kTouTiaoSeniorKey = kTouTiaoSeniorKey;
            }

            public String getKGDTMobSDKAppKey() {
                return kGDTMobSDKAppKey;
            }

            public void setKGDTMobSDKAppKey(String kGDTMobSDKAppKey) {
                this.kGDTMobSDKAppKey = kGDTMobSDKAppKey;
            }

            public String getKGDTMobSDKChaPingKey() {
                return kGDTMobSDKChaPingKey;
            }

            public void setKGDTMobSDKChaPingKey(String kGDTMobSDKChaPingKey) {
                this.kGDTMobSDKChaPingKey = kGDTMobSDKChaPingKey;
            }

            public String getKGDTMobSDKKaiPingKey() {
                return kGDTMobSDKKaiPingKey;
            }

            public void setKGDTMobSDKKaiPingKey(String kGDTMobSDKKaiPingKey) {
                this.kGDTMobSDKKaiPingKey = kGDTMobSDKKaiPingKey;
            }

            public String getKGDTMobSDKBannerKey() {
                return kGDTMobSDKBannerKey;
            }

            public void setKGDTMobSDKBannerKey(String kGDTMobSDKBannerKey) {
                this.kGDTMobSDKBannerKey = kGDTMobSDKBannerKey;
            }

            public String getKGDTMobSDKNativeKey() {
                return kGDTMobSDKNativeKey;
            }

            public void setKGDTMobSDKNativeKey(String kGDTMobSDKNativeKey) {
                this.kGDTMobSDKNativeKey = kGDTMobSDKNativeKey;
            }

            public String getKGDTMobSDKJiLiKey() {
                return kGDTMobSDKJiLiKey;
            }

            public void setKGDTMobSDKJiLiKey(String kGDTMobSDKJiLiKey) {
                this.kGDTMobSDKJiLiKey = kGDTMobSDKJiLiKey;
            }
        }
    }
}
