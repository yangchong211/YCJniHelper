

#define MSPONGE_TAG "msponge"
enum AllocatorType : char {
    // BumpPointer spaces are currently only used for ZygoteSpace construction.
    kAllocatorTypeBumpPointer,  // Use global CAS-based BumpPointer allocator. (*)
    kAllocatorTypeTLAB,  // Use TLAB allocator within BumpPointer space. (*)
    kAllocatorTypeRosAlloc,  // Use RosAlloc (segregated size, free list) allocator. (*)
    kAllocatorTypeDlMalloc,  // Use dlmalloc (well-known C malloc) allocator. (*)
    kAllocatorTypeNonMoving,  // Special allocator for non moving objects.
    kAllocatorTypeLOS,  // Large object space.
    // The following differ from the BumpPointer allocators primarily in that memory is
    // allocated from multiple regions, instead of a single contiguous space.
    kAllocatorTypeRegion,  // Use CAS-based contiguous bump-pointer allocation within a region. (*)
    kAllocatorTypeRegionTLAB,  // Use region pieces as TLABs. Default for most small objects. (*)
};

typedef void *(*los_alloc)(void *, void *, size_t, size_t *, size_t *, size_t *);

typedef void *(*out_of_memory)(void *, void *, size_t, enum AllocatorType);

typedef void *(*alloc_internal_with_gc_type)(void *, void *,
                                             enum AllocatorType,
                                             bool,
                                             size_t,
                                             size_t *,
                                             size_t *,
                                             size_t *,
                                             void *);

typedef void *(*grow_for_utilization)(void *, void *, uint64_t);

void *los_alloc_proxy(void *thiz, void *self, size_t num_bytes, size_t *bytes_allocated,
                      size_t *usable_size,
                      size_t *bytes_tl_bulk_allocated);


void *allocate_internal_with_gc_proxy(void *heap, void *self,
                                      enum AllocatorType allocator,
                                      bool instrumented,
                                      size_t alloc_size,
                                      size_t *bytes_allocated,
                                      size_t *usable_size,
                                      size_t *bytes_tl_bulk_allocated,
                                      void *klass);

void throw_out_of_memory_error_proxy(void *heap, void *self, size_t byte_count,
                                     enum AllocatorType allocator_type);

void
grow_for_utilization_proxy(void *heap, void *collector_ran, uint64_t bytes_allocated_before_gc);


uint64_t get_num_bytes_allocated(void *);

void call_record_free(void *,int64_t);
