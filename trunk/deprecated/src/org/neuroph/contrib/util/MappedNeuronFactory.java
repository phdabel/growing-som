package org.neuroph.contrib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.Properties;

public class MappedNeuronFactory {

	@SuppressWarnings("unchecked")
	public static MappedCompetitiveNeuron createNeuron(NeuronProperties neuronProperties, Position position) {

		MappedCompetitiveNeuron neuron;
		
		InputFunction inputFunction = null;
		Class<? extends InputFunction> inputFunctionClass = neuronProperties.getInputFunction();
		if (inputFunctionClass != null) {
			inputFunction = createInputFunction(inputFunctionClass);
		}

		TransferFunction transferFunction = createTransferFunction(neuronProperties.getTransferFunctionProperties());
		// use three param constructor to create neuron
		try {
			neuron = new MappedCompetitiveNeuron(inputFunction, transferFunction, position.getAxisPosition());
			neuron.setError(0.0);
		}  catch (NeurophException error){
			
			throw new NeurophException("Some Neuroph Exception!");			
		}

		return neuron;                    

	}

	private static InputFunction createInputFunction(Class<? extends InputFunction> inputFunctionClass) {
		InputFunction inputFunction = null;

		try {
			inputFunction = inputFunctionClass.newInstance();
		} catch (InstantiationException e) {
			System.err.println("InstantiationException while creating InputFunction!");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("No permission to invoke method");
			e.printStackTrace();
		}

		return inputFunction;
	}


	/**
	 * Creates and returns instance of transfer function
	 * 
	 * @param tfProperties
	 *            transfer function properties
	 * @return returns transfer function
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static TransferFunction createTransferFunction(Properties tfProperties) {
		TransferFunction transferFunction = null;

		Class  tfClass = (Class)tfProperties.getProperty("transferFunction");

		try {
			Class[] paramTypes = null;

			Constructor[] cons = tfClass.getConstructors();
			for (int i=0; i<cons.length; i++) {
				paramTypes = cons[i].getParameterTypes();

				// use constructor with one parameter of Properties type
				if ((paramTypes.length == 1) && (paramTypes[0] == Properties.class)) {
					Class argTypes[] = new Class[1];
					argTypes[0] = Properties.class;
					Constructor ct = tfClass.getConstructor(argTypes);

					Object argList[] = new Object[1];
					argList[0] = tfProperties;
					transferFunction = (TransferFunction) ct.newInstance(argList);
					break;
				} else if(paramTypes.length == 0) { // use constructor without params
					transferFunction = (TransferFunction) tfClass.newInstance();
				break;
				}
			}

			return transferFunction;

		} catch (NoSuchMethodException e) {
			System.err.println("getConstructor() couldn't find the constructor while creating TransferFunction!");
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.err.println("InstantiationException while creating TransferFunction!");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("No permission to invoke method while creating TransferFunction!");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.err.println("Method threw an: " + e.getTargetException() + " while creating TransferFunction!");
			e.printStackTrace();
		}

		return transferFunction;
	}

}
