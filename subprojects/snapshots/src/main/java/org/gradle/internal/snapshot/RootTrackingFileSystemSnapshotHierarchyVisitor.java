/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.snapshot;

public interface RootTrackingFileSystemSnapshotHierarchyVisitor {
    /**
     * Called before visiting the contents of a directory.
     */
    default void enterDirectory(CompleteDirectorySnapshot directorySnapshot, boolean isRoot) {}

    /**
     * Called for each regular file/directory/missing/unavailable file.
     *
     * @return how to continue visiting the rest of the snapshot hierarchy.
     */
    SnapshotVisitResult visitEntry(CompleteFileSystemLocationSnapshot snapshot, boolean isRoot);

    /**
     * Called after all entries in the directory has been visited.
     */
    default void leaveDirectory(CompleteDirectorySnapshot directorySnapshot, boolean isRoot) {}

    static FileSystemSnapshotHierarchyVisitor asSimpleHierarchyVisitor(RootTrackingFileSystemSnapshotHierarchyVisitor delegate) {
        return new FileSystemSnapshotHierarchyVisitor() {
            private int treeDepth;

            @Override
            public void enterDirectory(CompleteDirectorySnapshot directorySnapshot) {
                delegate.enterDirectory(directorySnapshot, isRoot());
                treeDepth++;
            }

            @Override
            public SnapshotVisitResult visitEntry(CompleteFileSystemLocationSnapshot snapshot) {
                return delegate.visitEntry(snapshot, isRoot());
            }

            @Override
            public void leaveDirectory(CompleteDirectorySnapshot directorySnapshot) {
                treeDepth--;
                delegate.leaveDirectory(directorySnapshot, isRoot());
            }

            private boolean isRoot() {
                return treeDepth == 0;
            }
        };
    }
}
