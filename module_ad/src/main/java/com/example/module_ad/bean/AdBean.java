package com.example.module_ad.bean;

import com.example.module_ad.base.AdActionBean;
import com.example.module_ad.base.AdTypeBean;

/**
 * @author wujinming QQ:1245074510
 * @name Wifi_Manager
 * @class name：com.example.module_ad.bean
 * @class describe
 * @time 2021/2/5 14:37:30
 * @class describe
 */
public class AdBean {

    /**
     * code : 0
     * message : 成功
     * data : {"start_page":{"spread_screen":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":300,"ad_percent":"0_100"}},"see_detail":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}},"clean_finished":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}},"cooling_page":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}},"cooling_finished":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"self_insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"}},"battery_page":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"banner_screen":{"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}},"powersaving_page":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}},"charge_page":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"banner_screen":{"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}},"commonly_used_page_second_level":{"banner_screen":{"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}},"video_management":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}},"software_management_page":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}},"wx_qq_shortvideo_package_picture_page":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}},"album_video_music_file_package_page":{"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}},"exit_page":{"native_screen":{"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}},"setting_page":{"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}},"Advertisement":{"kTouTiaoAppKey":"5143358","kTouTiaoKaiPing":"887434234","kTouTiaoBannerKey":"945825052","kTouTiaoChaPingKey":"945825055","kTouTiaoJiLiKey":"945825058","kTouTiaoSeniorKey":"945825048","kTouTiaoSeniorLockKey":"","kTouTiaoSeniorUnlockKey":"","ktouTiaoFullscreenvideoKey":"","kGDTMobSDKAppKey":"1111421253","kGDTMobSDKChaPingKey":"5021860026386633","kGDTMobSDKKaiPingKey":"8011265076387557","kGDTMobSDKBannerKey":"4061860026891153","kGDTMobSDKNativeKey":"8071661066586630","kGDTMobSDKNativeSmallKey":"1071064036383616","kGDTMobSDKJiLiKey":"4031965026883642"}}
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
         * start_page : {"spread_screen":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":300,"ad_percent":"0_100"}}
         * see_detail : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}}
         * clean_finished : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}}
         * cooling_page : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}}
         * cooling_finished : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"self_insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"}}
         * battery_page : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"banner_screen":{"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}}
         * powersaving_page : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}}
         * charge_page : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"banner_screen":{"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}}
         * commonly_used_page_second_level : {"banner_screen":{"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}}
         * video_management : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}}
         * software_management_page : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"},"insert_screen":{"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"},"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}}
         * wx_qq_shortvideo_package_picture_page : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}}
         * album_video_music_file_package_page : {"native_advertising":{"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}}
         * exit_page : {"native_screen":{"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}}
         * setting_page : {"incentive_video":{"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}}
         * Advertisement : {"kTouTiaoAppKey":"5143358","kTouTiaoKaiPing":"887434234","kTouTiaoBannerKey":"945825052","kTouTiaoChaPingKey":"945825055","kTouTiaoJiLiKey":"945825058","kTouTiaoSeniorKey":"945825048","kTouTiaoSeniorLockKey":"","kTouTiaoSeniorUnlockKey":"","ktouTiaoFullscreenvideoKey":"","kGDTMobSDKAppKey":"1111421253","kGDTMobSDKChaPingKey":"5021860026386633","kGDTMobSDKKaiPingKey":"8011265076387557","kGDTMobSDKBannerKey":"4061860026891153","kGDTMobSDKNativeKey":"8071661066586630","kGDTMobSDKNativeSmallKey":"1071064036383616","kGDTMobSDKJiLiKey":"4031965026883642"}
         */

        private StartPageBean start_page;
        private SeeDetailBean see_detail;
        private CleanFinishedBean clean_finished;
        private CoolingPageBean cooling_page;
        private CoolingFinishedBean cooling_finished;
        private BatteryPageBean battery_page;
        private PowersavingPageBean powersaving_page;
        private ChargePageBean charge_page;
        private CommonlyUsedPageSecondLevelBean commonly_used_page_second_level;
        private VideoManagementBean video_management;
        private SoftwareManagementPageBean software_management_page;
        private WxQqShortvideoPackagePicturePageBean wx_qq_shortvideo_package_picture_page;
        private AlbumVideoMusicFilePackagePageBean album_video_music_file_package_page;
        private ExitPageBean exit_page;
        private SettingPageBean setting_page;
        private AdvertisementBean Advertisement;

        public StartPageBean getStart_page() {
            return start_page;
        }

        public void setStart_page(StartPageBean start_page) {
            this.start_page = start_page;
        }

        public SeeDetailBean getSee_detail() {
            return see_detail;
        }

        public void setSee_detail(SeeDetailBean see_detail) {
            this.see_detail = see_detail;
        }

        public CleanFinishedBean getClean_finished() {
            return clean_finished;
        }

        public void setClean_finished(CleanFinishedBean clean_finished) {
            this.clean_finished = clean_finished;
        }

        public CoolingPageBean getCooling_page() {
            return cooling_page;
        }

        public void setCooling_page(CoolingPageBean cooling_page) {
            this.cooling_page = cooling_page;
        }

        public CoolingFinishedBean getCooling_finished() {
            return cooling_finished;
        }

        public void setCooling_finished(CoolingFinishedBean cooling_finished) {
            this.cooling_finished = cooling_finished;
        }

        public BatteryPageBean getBattery_page() {
            return battery_page;
        }

        public void setBattery_page(BatteryPageBean battery_page) {
            this.battery_page = battery_page;
        }

        public PowersavingPageBean getPowersaving_page() {
            return powersaving_page;
        }

        public void setPowersaving_page(PowersavingPageBean powersaving_page) {
            this.powersaving_page = powersaving_page;
        }

        public ChargePageBean getCharge_page() {
            return charge_page;
        }

        public void setCharge_page(ChargePageBean charge_page) {
            this.charge_page = charge_page;
        }

        public CommonlyUsedPageSecondLevelBean getCommonly_used_page_second_level() {
            return commonly_used_page_second_level;
        }

        public void setCommonly_used_page_second_level(CommonlyUsedPageSecondLevelBean commonly_used_page_second_level) {
            this.commonly_used_page_second_level = commonly_used_page_second_level;
        }

        public VideoManagementBean getVideo_management() {
            return video_management;
        }

        public void setVideo_management(VideoManagementBean video_management) {
            this.video_management = video_management;
        }

        public SoftwareManagementPageBean getSoftware_management_page() {
            return software_management_page;
        }

        public void setSoftware_management_page(SoftwareManagementPageBean software_management_page) {
            this.software_management_page = software_management_page;
        }

        public WxQqShortvideoPackagePicturePageBean getWx_qq_shortvideo_package_picture_page() {
            return wx_qq_shortvideo_package_picture_page;
        }

        public void setWx_qq_shortvideo_package_picture_page(WxQqShortvideoPackagePicturePageBean wx_qq_shortvideo_package_picture_page) {
            this.wx_qq_shortvideo_package_picture_page = wx_qq_shortvideo_package_picture_page;
        }

        public AlbumVideoMusicFilePackagePageBean getAlbum_video_music_file_package_page() {
            return album_video_music_file_package_page;
        }

        public void setAlbum_video_music_file_package_page(AlbumVideoMusicFilePackagePageBean album_video_music_file_package_page) {
            this.album_video_music_file_package_page = album_video_music_file_package_page;
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
             * spread_screen : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":300,"ad_percent":"0_100"}
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
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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

        public static class SeeDetailBean extends AdTypeBean {
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             * insert_screen : {"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"}
             * incentive_video : {"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;
            private InsertScreenBean insert_screen;
            private IncentiveVideoBean incentive_video;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            public InsertScreenBean getInsert_screen() {
                return insert_screen;
            }

            public void setInsert_screen(InsertScreenBean insert_screen) {
                this.insert_screen = insert_screen;
            }

            public IncentiveVideoBean getIncentive_video() {
                return incentive_video;
            }

            public void setIncentive_video(IncentiveVideoBean incentive_video) {
                this.incentive_video = incentive_video;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return insert_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return insert_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return incentive_video;
            }

            public static class NativeAdvertisingBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }

            public static class InsertScreenBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 150
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
                    return 0;
                }
            }

            public static class IncentiveVideoBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 60
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
                    return 0;
                }
            }
        }

        public static class CleanFinishedBean extends AdTypeBean {
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             * insert_screen : {"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"}
             * incentive_video : {"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;
            private InsertScreenBean insert_screen;
            private IncentiveVideoBean incentive_video;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            public InsertScreenBean getInsert_screen() {
                return insert_screen;
            }

            public void setInsert_screen(InsertScreenBean insert_screen) {
                this.insert_screen = insert_screen;
            }

            public IncentiveVideoBean getIncentive_video() {
                return incentive_video;
            }

            public void setIncentive_video(IncentiveVideoBean incentive_video) {
                this.incentive_video = incentive_video;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return insert_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return incentive_video;
            }

            public static class NativeAdvertisingBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }

            public static class InsertScreenBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 150
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
                    return 0;
                }
            }

            public static class IncentiveVideoBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 60
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
                    return 0;
                }
            }
        }

        public static class CoolingPageBean extends AdTypeBean {
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             * insert_screen : {"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"}
             * incentive_video : {"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;
            private InsertScreenBean insert_screen;
            private IncentiveVideoBean incentive_video;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            public InsertScreenBean getInsert_screen() {
                return insert_screen;
            }

            public void setInsert_screen(InsertScreenBean insert_screen) {
                this.insert_screen = insert_screen;
            }

            public IncentiveVideoBean getIncentive_video() {
                return incentive_video;
            }

            public void setIncentive_video(IncentiveVideoBean incentive_video) {
                this.incentive_video = incentive_video;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return insert_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return incentive_video;
            }

            public static class NativeAdvertisingBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }

            public static class InsertScreenBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 150
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
                    return 0;
                }
            }

            public static class IncentiveVideoBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 60
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
                    return 0;
                }
            }
        }

        public static class CoolingFinishedBean extends AdTypeBean {
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             * self_insert_screen : {"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;
            private SelfInsertScreenBean self_insert_screen;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            public SelfInsertScreenBean getSelf_insert_screen() {
                return self_insert_screen;
            }

            public void setSelf_insert_screen(SelfInsertScreenBean self_insert_screen) {
                this.self_insert_screen = self_insert_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return self_insert_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return self_insert_screen;
            }

            public static class NativeAdvertisingBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }

            public static class SelfInsertScreenBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 150
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
                    return 0;
                }
            }
        }

        public static class BatteryPageBean extends AdTypeBean {
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             * banner_screen : {"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;
            private BannerScreenBean banner_screen;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            public BannerScreenBean getBanner_screen() {
                return banner_screen;
            }

            public void setBanner_screen(BannerScreenBean banner_screen) {
                this.banner_screen = banner_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return banner_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_advertising;
            }

            public static class NativeAdvertisingBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }

            public static class BannerScreenBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 200
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
                    return 0;
                }
            }
        }

        public static class PowersavingPageBean extends AdTypeBean{
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             * insert_screen : {"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"}
             * incentive_video : {"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;
            private InsertScreenBean insert_screen;
            private IncentiveVideoBean incentive_video;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            public InsertScreenBean getInsert_screen() {
                return insert_screen;
            }

            public void setInsert_screen(InsertScreenBean insert_screen) {
                this.insert_screen = insert_screen;
            }

            public IncentiveVideoBean getIncentive_video() {
                return incentive_video;
            }

            public void setIncentive_video(IncentiveVideoBean incentive_video) {
                this.incentive_video = incentive_video;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return insert_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return incentive_video;
            }

            public static class NativeAdvertisingBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }

            public static class InsertScreenBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 150
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
                    return 0;
                }
            }

            public static class IncentiveVideoBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 60
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
                    return 0;
                }
            }
        }

        public static class ChargePageBean extends AdTypeBean{
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             * banner_screen : {"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;
            private BannerScreenBean banner_screen;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            public BannerScreenBean getBanner_screen() {
                return banner_screen;
            }

            public void setBanner_screen(BannerScreenBean banner_screen) {
                this.banner_screen = banner_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return banner_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_advertising;
            }

            public static class NativeAdvertisingBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }

            public static class BannerScreenBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 200
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
                    return 0;
                }
            }
        }

        public static class CommonlyUsedPageSecondLevelBean extends AdTypeBean{
            /**
             * banner_screen : {"status":true,"ad_origin":"gdt_toutiao","times":200,"change_times":300,"ad_percent":"0_100"}
             */

            private BannerScreenBean banner_screen;

            public BannerScreenBean getBanner_screen() {
                return banner_screen;
            }

            public void setBanner_screen(BannerScreenBean banner_screen) {
                this.banner_screen = banner_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return banner_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return banner_screen;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return banner_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return banner_screen;
            }

            public static class BannerScreenBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 200
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
                    return 0;
                }
            }
        }

        public static class VideoManagementBean extends AdTypeBean {
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_advertising;
            }

            public static class NativeAdvertisingBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }
        }

        public static class SoftwareManagementPageBean extends AdTypeBean{
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             * insert_screen : {"status":true,"ad_origin":"gdt_toutiao","times":150,"change_times":3,"ad_percent":"0_100"}
             * incentive_video : {"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;
            private InsertScreenBean insert_screen;
            private IncentiveVideoBean incentive_video;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            public InsertScreenBean getInsert_screen() {
                return insert_screen;
            }

            public void setInsert_screen(InsertScreenBean insert_screen) {
                this.insert_screen = insert_screen;
            }

            public IncentiveVideoBean getIncentive_video() {
                return incentive_video;
            }

            public void setIncentive_video(IncentiveVideoBean incentive_video) {
                this.incentive_video = incentive_video;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return insert_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return incentive_video;
            }

            public static class NativeAdvertisingBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }

            public static class InsertScreenBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 150
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
                    return 0;
                }
            }

            public static class IncentiveVideoBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 60
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
                    return 0;
                }
            }
        }

        public static class WxQqShortvideoPackagePicturePageBean extends AdTypeBean {
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_advertising;
            }

            public static class NativeAdvertisingBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }
        }

        public static class AlbumVideoMusicFilePackagePageBean extends AdTypeBean {
            /**
             * native_advertising : {"status":true,"ad_origin":"gdt_toutiao","times":0,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeAdvertisingBean native_advertising;

            public NativeAdvertisingBean getNative_advertising() {
                return native_advertising;
            }

            public void setNative_advertising(NativeAdvertisingBean native_advertising) {
                this.native_advertising = native_advertising;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_advertising;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_advertising;
            }

            public static class NativeAdvertisingBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 0
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
                    return 0;
                }
            }
        }

        public static class ExitPageBean extends AdTypeBean {
            /**
             * native_screen : {"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeScreenBean native_screen;

            public NativeScreenBean getNative_screen() {
                return native_screen;
            }

            public void setNative_screen(NativeScreenBean native_screen) {
                this.native_screen = native_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_screen;
            }

            public static class NativeScreenBean extends AdActionBean{
                /**
                 * status : true
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
                    return 0;
                }
            }
        }

        public static class SettingPageBean extends AdTypeBean {
            /**
             * incentive_video : {"status":true,"ad_origin":"gdt_toutiao","times":60,"change_times":10,"ad_percent":"0_100"}
             */

            private IncentiveVideoBean incentive_video;

            public IncentiveVideoBean getIncentive_video() {
                return incentive_video;
            }

            public void setIncentive_video(IncentiveVideoBean incentive_video) {
                this.incentive_video = incentive_video;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return incentive_video;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return incentive_video;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return incentive_video;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return incentive_video;
            }

            public static class IncentiveVideoBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 60
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
                    return 0;
                }
            }
        }

        public static class AdvertisementBean {
            /**
             * kTouTiaoAppKey : 5143358
             * kTouTiaoKaiPing : 887434234
             * kTouTiaoBannerKey : 945825052
             * kTouTiaoChaPingKey : 945825055
             * kTouTiaoJiLiKey : 945825058
             * kTouTiaoSeniorKey : 945825048
             * kTouTiaoSeniorLockKey :
             * kTouTiaoSeniorUnlockKey :
             * ktouTiaoFullscreenvideoKey :
             * kGDTMobSDKAppKey : 1111421253
             * kGDTMobSDKChaPingKey : 5021860026386633
             * kGDTMobSDKKaiPingKey : 8011265076387557
             * kGDTMobSDKBannerKey : 4061860026891153
             * kGDTMobSDKNativeKey : 8071661066586630
             * kGDTMobSDKNativeSmallKey : 1071064036383616
             * kGDTMobSDKJiLiKey : 4031965026883642
             */

            private String kTouTiaoAppKey;
            private String kTouTiaoKaiPing;
            private String kTouTiaoBannerKey;
            private String kTouTiaoChaPingKey;
            private String kTouTiaoJiLiKey;
            private String kTouTiaoSeniorKey;
            private String kTouTiaoSeniorLockKey;
            private String kTouTiaoSeniorUnlockKey;
            private String ktouTiaoFullscreenvideoKey;
            private String kGDTMobSDKAppKey;
            private String kGDTMobSDKChaPingKey;
            private String kGDTMobSDKKaiPingKey;
            private String kGDTMobSDKBannerKey;
            private String kGDTMobSDKNativeKey;
            private String kGDTMobSDKNativeSmallKey;
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

            public String getKTouTiaoSeniorLockKey() {
                return kTouTiaoSeniorLockKey;
            }

            public void setKTouTiaoSeniorLockKey(String kTouTiaoSeniorLockKey) {
                this.kTouTiaoSeniorLockKey = kTouTiaoSeniorLockKey;
            }

            public String getKTouTiaoSeniorUnlockKey() {
                return kTouTiaoSeniorUnlockKey;
            }

            public void setKTouTiaoSeniorUnlockKey(String kTouTiaoSeniorUnlockKey) {
                this.kTouTiaoSeniorUnlockKey = kTouTiaoSeniorUnlockKey;
            }

            public String getKtouTiaoFullscreenvideoKey() {
                return ktouTiaoFullscreenvideoKey;
            }

            public void setKtouTiaoFullscreenvideoKey(String ktouTiaoFullscreenvideoKey) {
                this.ktouTiaoFullscreenvideoKey = ktouTiaoFullscreenvideoKey;
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

            public String getKGDTMobSDKNativeSmallKey() {
                return kGDTMobSDKNativeSmallKey;
            }

            public void setKGDTMobSDKNativeSmallKey(String kGDTMobSDKNativeSmallKey) {
                this.kGDTMobSDKNativeSmallKey = kGDTMobSDKNativeSmallKey;
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
