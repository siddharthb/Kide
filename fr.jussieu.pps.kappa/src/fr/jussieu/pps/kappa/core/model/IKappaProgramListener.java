package fr.jussieu.pps.kappa.core.model;

/**
 * An ISmlProgramListener gets notified when an ISmlProgram is updated.
 */
public interface IKappaProgramListener {
	/** This method will be called by the program when it is updated. */
	public void programChanged (KappaProgram program);
}