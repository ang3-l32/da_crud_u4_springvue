<script setup>
import{ ref, onMounted } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

const libros = ref([]);

const nuevoLibro = ref({
  titulo: '',
  autor: '',
  categoria: '',
  editorial: '',
  nump: '',
  descripcion: '',
  imagenURL: ''
})

const editado = ref(false);


const cargarLibro = async () => {
  const response = await axios.get('http://localhost:8088/libro/traer-libro');
  libros.value = response.data;
  console.log(libros.value);
}

const agregarLibro = async () => {
  if(editado.value){
    await axios.put(`http://localhost:8088/libro/editar-libro/${nuevoLibro.value.id}`, nuevoLibro.value);
    Swal.fire({
      icon: 'success',
      title: 'Libro Actualizaso correctamente',
      showConfirmButton: false,
      timer: 1500
    })
    editado.value = false
  }else{
    await axios.post('http://localhost:8088/libro/insertar-libro', nuevoLibro.value);
    Swal.fire({
      icon: 'success',
      title: 'Libro agregado correctamente',
      showConfirmButton: false,
      timer: 1500
    })
  }

  await cargarLibro();
  nuevoLibro.value = {
    titulo: '',
    autor: '',
    categoria: '',
    editorial: '',
    nump: '',
    descripcion: '',
    imagenURL: ''
  };
}

const editarLibro = (lib) =>{
  nuevoLibro.value = lib;
  editado.value = true
}
const eliminarLibro = async (id) => {

  Swal.fire({
    title: '¿Estas seguro de Eliminar el Libro',
    text: "No podras revertir esto!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Si, Eliminarlo'
  }).then(async (result) =>{
    if (result.isConfirmed){
      await eliminarLibroPorId(id);
      Swal.fire(
        'Eliminado!',
        'El Libro ha sido Eliminado.',
        'success'
      )
    }
  })
  
}

const eliminarLibroPorId = async (id) =>{
  try{
    await axios.delete(`http://localhost:8088/libro/eliminar-libro/${id}`);
    Swal.fire({
      icon: 'success',
      title: 'Libro Eliminado Correctamente',
      showConfirmButton: false,
      timer: 1500
    })
    console.log('Libro Eliminado con id:', id);
    await cargarLibro();
  }catch (errr){
    console.error('Error al Eliminalr Libro:', errr)
    Swal.fire({
      icon: 'error',
      title: 'Errror al Eliminar Libro',
      text: 'No se pudo Eliminar el Libro'
    })
  }
}

onMounted(cargarLibro);

</script>

<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12 mt-4">
        <div class="card shadow p-4 mb-4">
          <h2 class="text-center">Formulario de Libros</h2>
          <form @submit.prevent="agregarLibro">
            <div class="row">
            <div class="col-md-6 mb-3">
              <label for="titulo" class="form-label">Titulo</label>
              <input type="text" class="form-control" id="titulo" v-model="nuevoLibro.titulo" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="autor" class="form-label">Autor</label>
              <input type="text" class="form-control" id="autor" v-model="nuevoLibro.autor" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="categoria" class="form-label">Categoria</label>
              <input type="text" class="form-control" id="categoria" v-model="nuevoLibro.categoria" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="editorial" class="form-label">Editorial</label>
              <input type="text" class="form-control" id="editorial" v-model="nuevoLibro.editorial" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="nump" class="form-label">Numero de Paginas</label>
              <input type="number" class="form-control" id="nump" v-model="nuevoLibro.nump" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="descripcion" class="form-label">Descripcion</label>
              <input type="text" class="form-control" id="descripcion" v-model="nuevoLibro.descripcion" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="imagenURL" class="form-label">imagen URL</label>
              <input type="text" class="form-control" id="imagenURL" v-model="nuevoLibro.imagenURL" required>
            </div>
            </div>
            <button type="submit" class="btn btn-primary">
              {{ editado ? 'Actualizar Libro' : 'Agregar Libro' }}
            </button>
          </form>
        </div>
      </div>
      <div class="col-md-12">
        <div class="card shadow">
          <div class="card-body">
            <h2 class="card-title mb-3 text-center">Tabla de Libros</h2>
              <table class="table table-hover align-middle">
                <thead class="lable-light">
                  <tr>
                    <th scope="col" class="text-center">Id</th>
                    <th scope="col" class="text-center">Titulo</th>
                    <th scope="col" class="text-center">Autor</th>
                    <th scope="col" class="text-center">Categoria</th>
                    <th scope="col" class="text-center">Editorial</th>
                    <th scope="col" class="text-center">Num de Paginas</th>
                    <th scope="col" class="text-center">Descripcion</th>
                    <th scope="col" class="text-center">Imagen</th>
                  </tr>
                 </thead>
                 <tbody>
                  <tr v-for="libro in libros" :key="libro.id">
                    <td>{{ libro.id }}</td>
                    <td>{{ libro.titulo }}</td>
                    <td>{{ libro.autor }}</td>
                    <td>{{ libro.categoria }}</td>
                    <td>{{ libro.editorial }}</td>
                    <td>{{ libro.nump }}</td>
                    <td>{{ libro.descripcion }}</td>
                    <td><img :src="libro.imagenURL" alt="Imagen de Libro" width="50"></td>
                    <td class="d-flex flex-column align-items-center gap-2">
                      <button @click="eliminarLibro(libro.id)" class="btn btn-danger">
                        <i class="bi bi-trash2-fill"></i>
                      </button>
                      <button @click="editarLibro(libro)" class="btn btn-warning">
                        <i class="bi bi-pencil-fill"></i>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* CONTENEDOR GENERAL */
.container {
  max-width: 1200px;
  margin: auto;
  padding: 2rem 1rem;
  background-color: #fdfaf5; /* tono cálido como pergamino */
  font-family: 'Georgia', serif;
}

/* TARJETAS */
.card {
  border-radius: 12px;
  border: none;
  background-color: #fff7ed;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
}

/* ENCABEZADOS */
h2, h5 {
  font-weight: bold;
  color: #4b3832; /* marrón libro antiguo */
  font-family: 'Merriweather', serif;
}

form label {
  font-weight: 600;
  color: #5a4e4b;
}

/* CAMPOS DE ENTRADA */
input.form-control {
  border-radius: 8px;
  padding: 0.5rem 1rem;
  border: 1px solid #ccc;
  background-color: #fffaf0;
}

/* BOTONES */
.btn-primary {
  background-color: #6f4e37; /* marrón libro antiguo */
  border: none;
  border-radius: 8px;
  padding: 0.5rem 1.2rem;
  font-weight: bold;
  color: white;
}

.btn-warning {
  background-color: #c19a6b;
  border: none;
  color: white;
}

.btn-danger {
  background-color: #8b0000;
  border: none;
  color: white;
}

.btn-warning, .btn-danger {
  border-radius: 8px;
}

/* TABLAS */
.table {
  border: 1px solid #ddd;
  border-radius: 10px;
  overflow: hidden;
  background-color: #fffef9;
}

.table th,
.table td {
  border: 1px solid #e3d9ce;
  padding: 12px;
  text-align: center;
  vertical-align: middle;
}

.table thead {
  background-color: #f0e4d7;
  color: #3e3e3e;
}

/* IMÁGENES */
img {
  border-radius: 5px;
  max-height: 100px;
}

/* FILAS FLEXIBLES */
td.d-flex {
  min-width: 70px;
  display: flex;
  justify-content: center;
  gap: 0.5rem;
}

/* CUERPO DE LA TARJETA */
.card-body {
  padding: 1rem;
  background-color: #fffef8;
  border-radius: 10px;
}

/* TARJETA CON SOMBRA DESTACADA */
.card.shadow {
  background-color: #f9f2e8;
  border-radius: 15px;
  box-shadow: 0 0 15px rgba(0,0,0,0.1);
}

</style>

