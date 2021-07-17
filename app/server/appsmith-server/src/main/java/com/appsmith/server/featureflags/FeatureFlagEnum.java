package com.appsmith.server.featureflags;

import org.ff4j.core.FlippingStrategy;
import org.ff4j.strategy.PonderationStrategy;
import org.ff4j.strategy.time.OfficeHourStrategy;

/**
 * This enum lists all the feature flags available along with their flipping strategy.
 * In order to create a new feature flag, create another enum entry and add the same string to {@link features/init-flags.yml}
 *
 * If you wish to define a custom flipping strategy, define a class that implements {@link FlippingStrategy} and
 * ensure that you've mentioned this custom class when defining the feature in {@link features/init-flags.yml}
 *
 * The feature flag implementation class should extend an existing feature flag implementation like {@link PonderationStrategy},
 * {@link OfficeHourStrategy} etc. These default classes provide a lot of basic functionality out of the box.
 *
 */
public enum FeatureFlagEnum {

    JS_EDITOR,
    // example feature flags
    WEIGHTAGE,
    COMMENT;
}

