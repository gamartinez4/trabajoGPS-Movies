package com.example.borrar

import androidx.navigation.navOptions

class Animations {

    companion object {

        val options_fade = navOptions {
            anim {
                enter = R.anim.fade_in
                exit = R.anim.fade_out
                popEnter = R.anim.fade_in
                popExit = R.anim.fade_out
            }
        }

        val options_slide_in = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
            }
        }

        val options_slide_out = navOptions {
            anim {
                enter = R.anim.slide_in_left
                exit = R.anim.slide_out_right
            }
        }
    }

}