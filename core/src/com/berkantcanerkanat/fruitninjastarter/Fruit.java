package com.berkantcanerkanat.fruitninjastarter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Fruit {
    public static double radius = 0.0f;
    float firstPosX;
    boolean shreded = false;
    //bu enum olayı ile yaptıgım her seyı string ile de yapabılırım
    //meyvelerım ve dusmanım var gıbı dusun bazıe meyveler 1 puan verırken bazıları 3 vercek bazıları ıse canımı alıcak
    //bunları ayırt etmek ıcın strıng de kullanabılırdım ama enum ıle daha kullanıslı
    public enum  Type{
        REGULAR,EXTRA,ENEMY,LIFE
    }
    Type type;
    //her objenin bir pozisyonu ve x ve y dogrultusunda hızı var bunun ıcın Vector2 classını kullanıcaz
    Vector2 pos,velocity;// Vector2 classı objelerının 2 ozelligi vardır soyle dusun pozisyon x ve y gıbı
    Fruit(Vector2 pos,Vector2 velocity){
        this.pos = pos;
        this.velocity = velocity;
        type = Type.REGULAR;
        firstPosX = pos.x;
    }
    public boolean isClicked(Vector2 clickedPos){//oyuncu meyveye dokundu mu
        if(pos.dst2(clickedPos) <= radius*radius+1) return true;
        //dst2() 2 vektor arası uzaklıgın karesını verır
        return false;
    }
    public final Vector2 getPos(){//meyvenin guncel pozisyonu
        return pos;
    }
    public boolean isOutOfScreen(){//ekran dısı meyve tespiti ve tam 0 da degıl bıraz daha ekran altında kontrol yapıyoruz
        return (pos.y < -2f * radius);
    }
    public void update(double deltaTime){
        velocity.y -= deltaTime*Gdx.graphics.getWidth()*0.2f;

        if(firstPosX <= Gdx.graphics.getWidth()*0.5f){
            velocity.x += deltaTime*Gdx.graphics.getWidth()*0.1f;
        }else{
            velocity.x -= deltaTime*Gdx.graphics.getWidth()*0.1f;
        }

        pos.mulAdd(velocity, (float) deltaTime);
        //mulAdd() pozisyon vektorunu hıza gore guncelleyecegız
    }

}
