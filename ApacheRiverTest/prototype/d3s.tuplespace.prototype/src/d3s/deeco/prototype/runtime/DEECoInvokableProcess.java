package d3s.deeco.prototype.runtime;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import d3s.deeco.prototype.knowledge.IKnowledgeManager;
import d3s.deeco.prototype.knowledge.Knowledge;
import d3s.deeco.prototype.properties.DEECoProperty;

public class DEECoInvokableProcess extends DEECoInvokable {

	private IKnowledgeManager km;

	public Long rootKnowledgeId;
	public List<DEECoProperty> inputParameters;
	public List<DEECoProperty> outputParameters;

	public Method method;

	public DEECoInvokableProcess(IKnowledgeManager km) {
		this.km = km;
	}

	@Override
	protected void invoke() {
		try {
			List<Object> input = km.readProperties(this);
			int parameterListLength = method.getParameterTypes().length;
			Object[] methodParameters = new Object[parameterListLength];
			for (int i = 0; i < inputParameters.size(); i++) {
				methodParameters[inputParameters.get(i).index] = input.get(i);
			}
			Knowledge di;
			for (DEECoProperty dp : outputParameters) {
				if (methodParameters[dp.index] == null) {
					di = (Knowledge) dp.type.newInstance();
					di.id = km.getInterfaceIdFromPath(dp.name, rootKnowledgeId);
					di.typeDefinition = dp.type;
					methodParameters[dp.index] = dp.type.newInstance();
				}
			}
			method.invoke(method, methodParameters);
			List<Object> output = new ArrayList<Object>();
			for (DEECoProperty dp : outputParameters) {
				output.add(methodParameters[dp.index]);
			}
			km.writeProperties(this, output);
		} catch (Exception e) {
			System.out.println("Error - Process invocation exception: "
					+ e.getMessage());
		}
	}
}
