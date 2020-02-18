package org.litesoft.persisted;

import org.litesoft.bean.Id32Accessor;

public interface IPersistedObjectId32 extends IPersistedObjectIdCommon<Integer>,
                                              Id32Accessor {

    abstract class AbstractBuilder<T extends IPersistedObjectId32, B extends POBuilder<T>>
            extends AbstractIdAndVersionBuilder<Integer, T, B> implements Id32Accessor {

        protected AbstractBuilder( IPersistedObjectId32 them ) {
            super( them );
        }

        @Override
        public Integer getId() {
            return super.getId();
        }
    }
}
