<template>
  <div class="todo-form">
    <h2>{{ isEditing ? '할일 수정' : '새 할일 추가' }}</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="title">제목 *</label>
        <input
          id="title"
          v-model="formData.title"
          type="text"
          placeholder="할일 제목을 입력하세요"
          required
          maxlength="200"
        />
      </div>

      <div class="form-group">
        <label for="description">설명</label>
        <textarea
          id="description"
          v-model="formData.description"
          placeholder="할일 설명을 입력하세요 (선택사항)"
          rows="4"
          maxlength="1000"
        ></textarea>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn btn-primary">
          {{ isEditing ? '수정' : '추가' }}
        </button>
        <button
          v-if="isEditing"
          type="button"
          class="btn btn-secondary"
          @click="handleCancel"
        >
          취소
        </button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: 'TodoForm',
  props: {
    editTodo: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      formData: {
        title: '',
        description: '',
        completed: false
      }
    };
  },
  computed: {
    isEditing() {
      return this.editTodo !== null;
    }
  },
  watch: {
    editTodo: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            title: newVal.title,
            description: newVal.description || '',
            completed: newVal.completed
          };
        } else {
          this.resetForm();
        }
      }
    }
  },
  methods: {
    handleSubmit() {
      if (this.isEditing) {
        this.$emit('update', { ...this.formData });
      } else {
        this.$emit('create', { ...this.formData });
      }
      this.resetForm();
    },
    handleCancel() {
      this.$emit('cancel');
      this.resetForm();
    },
    resetForm() {
      this.formData = {
        title: '',
        description: '',
        completed: false
      };
    }
  }
};
</script>

<style scoped>
.todo-form {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.todo-form h2 {
  margin: 0 0 1.5rem 0;
  color: #2c3e50;
  font-size: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
  font-family: inherit;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #42b983;
}

.form-actions {
  display: flex;
  gap: 1rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: linear-gradient(135deg, #42b983 0%, #35a372 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(66, 185, 131, 0.4);
}

.btn-secondary {
  background: #f5f5f5;
  color: #666;
}

.btn-secondary:hover {
  background: #e0e0e0;
}
</style>
