package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.AbstractAbility;
import game_objects.abilities.BasicMelee;
import game_objects.abilities.ComboStrike;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CloakAndDagger extends AbstractWeapon{
	
	ComboStrike comboAttack;
	Array<Float> comboAttackCd1, comboAttackCd2;
	
	public CloakAndDagger(ManipulatableObject parent, float width,
			float height, Vector2 positionOffset, Array<AbstractAbility> attacksInCombo, Array<Float> comboAttackCd1, Array<Float> comboAttackCd2) {
		super(parent, width, height, positionOffset);
		
		origin.set(0, dimension.y / 2);
		defaultAttackCooldown = .3f;
		image = Assets.instance.weapons.sword;
		moveUp();
		name = "Cloak and Dagger";
		this.comboAttackCd1 = comboAttackCd1;
		this.comboAttackCd2 = comboAttackCd2;
		comboAttack = new ComboStrike(this.parent, attacksInCombo, comboAttackCd1, comboAttackCd2);
		
		defaultAttackCooldown = 0;
		
	}
	
	@Override
	protected void defaultAttackInit(DIRECTION direction) {
		
		comboAttack.trigger(direction);
		
	}
	@Override
	public void update(float deltaTime) {
		comboAttack.update(deltaTime);
		super.update(deltaTime);
	}
	
	@Override
	protected void defaultAttackInit(Vector2 rightJoyStick) {
		// TODO Auto-generated method stub
		super.defaultAttackInit(rightJoyStick);
	}

}
