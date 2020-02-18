package org.litesoft.persisted;

import org.litesoft.bean.IdUuidAccessor;

public interface IPersistedObjectIdUuid extends IPersistedObjectIdCommon<String>,
                                                IdUuidAccessor {

    abstract class AbstractBuilder<T extends IPersistedObjectIdUuid, B extends POBuilder<T>>
            extends AbstractIdAndVersionBuilder<String, T, B> implements IdUuidAccessor {

        protected AbstractBuilder( IPersistedObjectIdUuid them ) {
            super( them );
        }

        @Override
        public String getId() {
            return super.getId();
        }
    }
}
