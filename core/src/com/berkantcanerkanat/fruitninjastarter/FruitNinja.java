package com.berkantcanerkanat.fruitninjastarter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FruitNinja extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture backGround;
	Texture apple,bill,cherry,ruby,heart;
	BitmapFont bitmapFont;
	FreeTypeFontGenerator freeTypeFontGenerator;

	int lives = 0;
	int score = 0;

	private double currentTime;
	private double gameOverTime = -1.0f;
	private double firstTime = TimeUtils.millis()/1000.0;

	float genCounter = 0;
	private final float startGenSpeed = 1.1f;//ilk defa meyve uretme hızım
	float genSpeed = startGenSpeed;//meyve uretme hızım

	Random random = new Random();
	Fruit [] fruitArray = new Fruit[5];

	int count = 0;

	@Override
	public void create () {
		backGround = new Texture("ninjabackground.png");
		batch = new SpriteBatch();
		apple = new Texture("apple.png");
		bill = new Texture("bill.png");
		cherry = new Texture("cherry.png");
		ruby = new Texture("ruby.png");
		heart = new Texture("heart.png");
		Fruit.radius = Math.max(Gdx.graphics.getWidth(),Gdx.graphics.getHeight())/20f;
		Gdx.input.setInputProcessor(this);
		freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("robotobold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.color = Color.WHITE;
		params.size = 60;
		params.characters = "0123456789 CsSorecutoplay.+-:";//hangi karakterleri kullanacagız
		bitmapFont = freeTypeFontGenerator.generateFont(params);
		addItem();
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(backGround,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		/*
		render() methodu habıre calısıyor ama her telefonda aynı hızda bu method habıre cagrılmıyor
		burada telefona ozgu olarak frame time ını alıyoruz kı ona ozgu ıslemler yapabılelım
		 */
		double newTime = TimeUtils.millis()/1000.0;
		double frameTime = Math.min(newTime-currentTime,0.3);
		double deltaTime = (float) frameTime;
		currentTime = newTime;

		if(lives <= 0 && gameOverTime == 0f){
			//gameover
			gameOverTime = currentTime;
			addItem();
			count = 0;
		}
		if(lives > 0){
			int heartX = 0;//1.canın bulundugu konum
			for(int i = 0 ;i<lives;i++){//yukarı solda canları gostermek ıcın
				batch.draw(heart,heartX,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/20,Gdx.graphics.getHeight()/20);
				heartX += Gdx.graphics.getWidth()/20;
			}
			if((newTime-firstTime) > 1.5){
				if(count < 5){
					count++;
				}
				firstTime = newTime;
			}
			    for(int i = 0 ;i<count;i++){
					fruitArray[i].update(deltaTime);
					switch (fruitArray[i].type){
						case REGULAR:
							batch.draw(apple,fruitArray[i].getPos().x,fruitArray[i].getPos().y,(float)Fruit.radius,(float)Fruit.radius);
							break;
						case EXTRA:
							batch.draw(cherry,fruitArray[i].getPos().x,fruitArray[i].getPos().y,(float)Fruit.radius,(float)Fruit.radius);
							break;
						case ENEMY:
							batch.draw(ruby,fruitArray[i].getPos().x,fruitArray[i].getPos().y,(float)Fruit.radius,(float)Fruit.radius);
							break;
						case LIFE:
							batch.draw(bill,fruitArray[i].getPos().x,fruitArray[i].getPos().y,(float)Fruit.radius,(float)Fruit.radius);
							break;
					}
					if(fruitArray[i].shreded || fruitArray[i].isOutOfScreen()){
						if(fruitArray[i].isOutOfScreen() && fruitArray[i].type == Fruit.Type.REGULAR){
							lives--;
						}
						fruitArray[i] = newItem();
					}

				}

		}
		bitmapFont.draw(batch,"Score: "+score,0,Gdx.graphics.getHeight()/10);
		if(lives <= 0){
			bitmapFont.draw(batch,"Cut to play",Gdx.graphics.getWidth()*0.5f-Gdx.graphics.getWidth()/7,Gdx.graphics.getHeight()*0.5f);//ekranın yarısına yazdırmak ıcın
		}
		batch.end();
	}
	public Fruit newItem(){
		float pos = random.nextFloat() * Math.max(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		Fruit item = new Fruit(new Vector2(pos,-10),new Vector2(50,500));
		item.shreded = false;
		float type = random.nextFloat();
		if(type > 0.98){
			item.type = Fruit.Type.LIFE;
		}else if(type > 0.82){
			item.type = Fruit.Type.EXTRA;
		}else if(type > 0.74){
			item.type = Fruit.Type.ENEMY;
		}
		return item;
	}
	public void addItem(){
		for(int i = 0 ;i<5;i++){
			float pos = random.nextFloat() * Math.max(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			Fruit item = new Fruit(new Vector2(pos,-10),new Vector2(50,500));
			item.shreded = false;
			float type = random.nextFloat();
			if(type > 0.98){
				item.type = Fruit.Type.LIFE;
			}else if(type > 0.82){
				item.type = Fruit.Type.EXTRA;
			}else if(type > 0.74){
				item.type = Fruit.Type.ENEMY;
			}
			fruitArray[i] = item;
		}
		//amacımız olasılık olarak cogu zaman elma olusması
		//sonra kucuk bır kısımda enemy
		//daha kucuk bır kısımda extra
		// neredeyse hic kımsında life olmasını ıstıyoruz bunu random ıle ayarlayacagız.
		// constructor ıcınde zaten regular oldugu ıcın onu yazmadım
	}
	@Override
	public void dispose () {
		batch.dispose();
		bitmapFont.dispose();
		freeTypeFontGenerator.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {//dokunup surukledik
		if(lives <= 0 && currentTime - gameOverTime > 2f){
			//Menu Mode
                gameOverTime = 0f;
                score = 0;
                lives = 4;
                genSpeed = startGenSpeed;
		}else{
			//oyun zamanı
			Vector2 touchedSpot = new Vector2(screenX,Gdx.graphics.getHeight()-screenY);
			System.out.println("bastigin x: "+screenX);
			System.out.println("bastigin y: "+(Gdx.graphics.getHeight()-screenY));
			for(int i = 0 ;i<fruitArray.length;i++){
				if(fruitArray[i].isClicked(touchedSpot) && fruitArray[i].shreded == false){
					System.out.println("degdin");
					fruitArray[i].shreded = true;
					switch (fruitArray[i].type){
						case REGULAR:
							score++;
							break;
						case EXTRA:
							score += 3;
							break;
						case ENEMY:
							lives--;
							break;
						case LIFE:
							lives++;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
