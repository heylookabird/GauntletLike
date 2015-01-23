package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.weapons.SwordAndShield;
import backend.Assets;
import backend.Calc;

import com.badlogic.gdx.math.Vector2;

public class ShieldThrow extends AbstractAbility {

	private ManipulatableObject target;
	private float time;
	private SwordAndShield swordAndShield;
	private int hitCount;
	private int hitCountMax;
	public ShieldThrow(ManipulatableObject parent, float x, float y,
			float width, float height, float speed, int hitCountMax, float range, SwordAndShield swordAndShield) {
		super(parent, x, y, width, height);
		time = 2f;
		lifeTimer = time;
		terminalVelocity.set(speed, speed);
		target = Calc.findClosestEnemy(this, parent.enemyTeamObjects);
		damage = 3;
		deletionTime = .03f;
		priority = 2;
		this.range = range;
		this.hitCountMax = hitCountMax;
		hitCount = 0;
		this.swordAndShield = swordAndShield;
		this.setImage(Assets.instance.effects.iceExplosionImgs.get(2));;
	
	}
	@Override
	public void update(float deltaTime) {
		
		if(target == null){
			removeThyself();
			return;
		}
		float angle = Calc.atan2(getCenter(), target.getCenter());
		System.out.println(angle);
		angle = angle < 0 ? angle + 360 : angle;
		velocity.x = Calc.cos(angle) * terminalVelocity.x;
		velocity.y = Calc.sin(angle) * terminalVelocity.y;
		
		
		super.update(deltaTime);
	}
	@Override
	protected void removeThyself() {
		swordAndShield.shieldOn = true;
		super.removeThyself();
	}
	@Override
	public void interact(AbstractGameObject couple) {

		if(couple instanceof ManipulatableObject){
			ManipulatableObject obj = (ManipulatableObject) couple;
			
			if(!this.isSameTeam(obj)){
				boolean newObj = isFirstInteraction(obj);
				
				target = Calc.findClosestEnemy(parent, parent.enemyTeamObjects, objectsAlreadyHit);
				lifeTimer = time;

				if(newObj){
					obj.takeHitFor(damage, this);
					hitCount++;
				}
				Vector2 cent = getCenter();
				if(hitCount > hitCountMax || (target != null && target.getCenter().dst2(cent.x, cent.y) > range * range)){
					removeThyself();
				}
				
			}
		}
		super.interact(couple);
	}

}
