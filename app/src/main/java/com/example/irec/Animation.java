package com.example.irec;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class Animation {
    private Context context;
    private ArrayList<AnimationKey> animationKeys;
    public class Skeleton {
        private Joint[] joints;
        public Skeleton(){

        }
        //public getPose()
    }
    public class Joint {
        public int[] jCoords;
        public int[] jPivot;
        public int jRotation;
        public Bitmap bodyPart;
        public Joint child;
        public Joint parent;
        public Joint(int[] jCoords, int[] jPivot, int jRotation, Bitmap bodyPart, Joint child, Joint parent ){
            this.jCoords = jCoords;
            this.jPivot = jPivot;
            this.jRotation = jRotation;
            this.bodyPart = bodyPart;
            this.child = child;
            this.parent = parent;
        }

    }

    public class AnimationKey {
        public int time;
        public Joint[] joints;
        public int[] rotationValues;
    }

}
