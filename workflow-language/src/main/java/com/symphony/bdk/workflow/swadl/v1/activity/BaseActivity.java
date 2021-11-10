package com.symphony.bdk.workflow.swadl.v1.activity;

import com.symphony.bdk.workflow.swadl.v1.Event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Base implementation of an activity providing data shared across all activities.
 * <p/>
 * This object is serialized/deserialized multiple times:
 * <p/>
 * 1. We read a SWADL file and built the Java model of it. At this stage the SWADL file might contain
 * references to variables (${}). So we cannot properly map variable references to the properties of an activity
 * (when they are longs or lists for instance). Instead, we store those properties in variableProperties.
 * <p/>
 * 2. We use the Java model to build a BPMN model, as part of it we serialize the activities as JSON.
 * <p/>
 * 3. When a workflow is executed, the BPMN model is used and upon execution, variables will be replaced. In the
 * CamundaExecutor we retrieve the JSON representation of an activity where variables have been replaced (by Camunda).
 * We then deserialize this JSON to the activity Java model, this time using the real properties and not
 * variableProperties as the variables have been replaced now.
 * <p/>
 * 4. In the activity executor we can access the activity's properties using proper types.
 */
@Data
public abstract class BaseActivity {
  /**
   * A unique identifier for an activity. Using camelCase.
   */
  @JsonProperty
  private String id;

  /**
   * The event triggering the activity's execution,
   * if not set the activity is triggered automatically once the activity before it finishes.
   */
  @Nullable
  @JsonProperty
  private Event on;

  @JsonProperty("if")
  @Nullable
  private String ifCondition;

  @JsonProperty("else")
  @Nullable
  private Object elseCondition;

  /**
   * Internal storage of activity's properties. <b>Not meant to be used in executors.</b>
   */
  @JsonIgnore
  private Map<String, Object> variableProperties = new HashMap<>();

  /**
   * Used by SwadlToBaseActivityMixin.
   */
  public void add(String key, Object value) {
    variableProperties.put(key, value);
  }

  @JsonIgnore
  public List<Event> getEvents() {
    if (on != null && on.getOneOf() != null) {
      return on.getOneOf();
    } else if (on != null) {
      return Collections.singletonList(on);
    } else {
      return Collections.emptyList();
    }
  }
}
