package com.feisukj.cleaning.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.filevisit.FileR
import java.io.File

class PathPhotoFragment:AbsPhotoFragment<ImageBean>() {
    companion object{
        private const val PATH_KEY="path_key"

        fun getInstance(path:Array<String>):PathPhotoFragment{
            val f=PathPhotoFragment()
            f.arguments= Bundle().apply {
                this.putStringArray(PATH_KEY,path)
            }
            return f
        }
    }

    var nullAction:(()->Unit)?=null

    override fun getPath(): List<String>? {
        return arguments?.getStringArray(PATH_KEY)?.toList()
    }

    override fun onNextFile(file: FileR): ImageBean? {
        return ImageBean(file)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onComplete() {

//        activity?.runOnUiThread {
//            nullAction={
//                if (adapter.getData().isEmpty()){
//                    cl_timeType.visibility= View.GONE
//                    tabRecyclerView.visibility = View.GONE
//                    rl_null.visibility= View.VISIBLE
//                }else{
//                    cl_timeType.visibility= View.VISIBLE
//                    tabRecyclerView.visibility= View.VISIBLE
//                    rl_null.visibility= View.GONE
//                }
//                activity?.let {
//                    AdController.Builder(it, ADConstants.COMMONLY_USED_PAGE_SECOND_LEVEL)
//                            .setContainer(frameAd)
//                            .create()
//                            .show()
//                }
//            }
//            if (tabRecyclerView!=null&&cl_timeType!=null&&rl_null!=null){
//                nullAction?.invoke()
//            }
//            adapter.isFinished = true
//            for ((key,value) in adapter.map){
//                if (value.currentView.id== R.id.load4)
//                    value.showNext()
//            }
//        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        nullAction?.invoke()
//    }
}