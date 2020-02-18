package org.litesoft.persisted;

import org.litesoft.bean.Id64Accessor;

public interface IPersistedObjectId64 extends IPersistedObjectIdCommon<Long>,
                                              Id64Accessor {

    abstract class AbstractBuilder<T extends IPersistedObjectId64, B extends POBuilder<T>>
            extends AbstractIdAndVersionBuilder<Long, T, B> implements Id64Accessor {

        protected AbstractBuilder( IPersistedObjectId64 them ) {
            super( them );
        }

        @Override
        public Long getId() {
            return super.getId();
        }
    }
}
