package ideal.vacuum.agent.behavior ;

import java.awt.Color ;

import ideal.vacuum.Ernest130Model ;
import ideal.vacuum.agent.DesignerListener ;
import ideal.vacuum.agent.GraphicProperties ;
import ideal.vacuum.agent.TactileEffect ;
import ideal.vacuum.agent.vision.PhotoreceptorCell ;

import javax.vecmath.Point3f ;
import javax.vecmath.Vector3f ;

/**
 * 
 * @author Joseph GARNIER
 * @version $Revision$
 */
public class BehaviorErnest9 extends AbstractBehavior {

	public BehaviorErnest9( Ernest130Model model , DesignerListener listener ) {
		super( model , listener ) ;
	}

	private void lookTheWorld() {
		GraphicProperties ernestGraphicProperties = this.model.getCopyOfGraphicProperties() ;
		PhotoreceptorCell[] retina = this.model.getRetina( ernestGraphicProperties
				.getmOrientation().z ) ;
		this.eyes.updateEye(
				retina[0].getxBlockPosition() ,
				retina[0].getyBlockPosition() ,
				retina[0].getBlockColor() ) ;
		this.notifyBehaviorStateChanged( new BehaviorStateChangeEvent( this , this
				.getCurrentBehaviorState() ) ) ;
	}

	private String getEyesStimuli() {
		return this.eyes.visualEffect().getLabel() ;
	}

	private void setLocationFromEyes() {
		this.effect.setLocation( this.eyes.getEventPosition() ) ;
	}

	protected void turnRight() {
		this.turnRightAnimWorld() ;
		this.lookTheWorld() ;
		//this.turnRightAnimFramesPlugins() ;

		String tactileStimuli = this.getEyesStimuli() ;
		this.effect.setLabel( tactileStimuli ) ;
		this.effect.setTransformation( (float) Math.PI / 2 , 0 ) ;
		this.setLocationFromEyes() ;
	}

	protected void turnLeft() {
		this.turnLeftAnimWorld() ;
		this.lookTheWorld() ;
		//this.turnLeftAnimFramesPlugins() ;

		String tactileStimuli = this.getEyesStimuli() ;
		this.effect.setLabel( tactileStimuli ) ;
		this.effect.setTransformation( (float) -Math.PI / 2 , 0 ) ;
		this.setLocationFromEyes() ;
	}

	protected void moveForward() {
		Vector3f localPoint = new Vector3f( this.model.DIRECTION_AHEAD ) ;
		Vector3f aheadPoint = this.model.localToParentRef( localPoint ) ;

		if ( this.model.getEnvironment().affordWalk( aheadPoint ) &&
				!this.model.affordCuddle( aheadPoint ) ) {
			this.moveForwardAnimWorld() ;
			this.lookTheWorld() ;
			//this.moveForwardAnimFramesPlugins() ;
			this.setLocationFromEyes() ;
			if ( this.model.getEnvironment().isFood( aheadPoint.x , aheadPoint.y ) ) {
				this.effect.setColor( this.model.getEnvironment()
						.seeBlock( aheadPoint.x , aheadPoint.y ).getRGB() ) ;
				this.effect.setLocation( new Point3f() ) ;
				this.model.getEnvironment().eatFood( aheadPoint ) ;
				this.effect.setLabel( TactileEffect.FOOD.getLabel() ) ;
			} else {
				String tactileStimuli = this.getEyesStimuli() ;
				this.effect.setLabel( tactileStimuli ) ;
			}
			this.effect.setTransformation( 0 , -1 ) ;

		} else {
			this.bumpAheadAnimWorld() ;
			this.lookTheWorld() ;
			//this.bumpAheadAnimFramesPlugins() ;
			this.effect.setLocation( new Point3f( 1 , 0 , 0 ) ) ;
			this.effect.setColor( Color.RED.getRGB() );
			this.effect.setLabel( TactileEffect.FALSE.getLabel() ) ;
		}
	}

	protected void moveBackward() {
	}

	protected void touch() {
	}

	protected void touchLeft() {
	}

	protected void touchRight() {
	}
}